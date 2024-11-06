package com.jasper.relatorio.service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.jasper.relatorio.model.dto.ProdutosRelatorioDTO;
import com.jasper.relatorio.model.dto.RelatorioDTO;
import com.jasper.relatorio.model.entity.Produto;
import com.jasper.relatorio.model.entity.Venda;
import com.jasper.relatorio.util.Utilitarios;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelatorioService {

    private static final String PATH_SUBRELATORIO_PRODUTOS = "classpath:relatorio/produtos.jrxml";
    private static final String PATH_RELATORIO_PRINCIPAL = "classpath:relatorio/relatorio.jrxml";
    private final ResourceLoader loader;

    public byte[] gerarImpressoVendas() {
        Venda venda = Venda.buscarVenda(); // simula a busca no banco da entidade
       return gerarRelatorioVenda(venda);
    }

    public byte[] gerarRelatorioVenda(Venda venda) {

        if (Objects.isNull(venda)) {
            throw new RuntimeException("Não é possivel gerar esse relatorio porque a venda não existe.");
        }
        try {
            RelatorioDTO dadosRelatorio = bindDadosRelatorio(venda); // Monta os dados do relatorio
            return gerarBytesPdfCertificado(dadosRelatorio); // Gera o array de bites do relatorio em PDF

        } catch (Exception e) {
            log.error("Error: ", e);
            throw new RuntimeException("Error ao gerar o relatório, consulte o administrador do sistema.");
        }
    }
    
    private byte[] gerarBytesPdfCertificado(RelatorioDTO dadosRelatorioo) {
          return generateReportVenda(Arrays.asList(dadosRelatorioo), PATH_RELATORIO_PRINCIPAL);
    }
        
    public byte[] generateReportVenda(List<RelatorioDTO> dados, String pathRelatorio) {
        try {
            InputStream inputStream = loader
            .getResource(pathRelatorio)
            .getInputStream(); // Lê o arquivo jrxml principal do relatorio
            JasperReport relatorioPrincipalComnpilado = JasperCompileManager.compileReport(inputStream); // compila para .jasper
            
            InputStream inputStreamSubreportProdutos = loader
            .getResource(PATH_SUBRELATORIO_PRODUTOS) // lê o subrelatorio de produtos
            .getInputStream();
            JasperReport subReportProdutos = JasperCompileManager.compileReport(inputStreamSubreportProdutos); // compila para .jasper
            
            Map<String, Object> params = new HashMap<>();
            
            params.put("PRODUTOS", subReportProdutos);
            
            JRDataSource dataSource = new JRBeanCollectionDataSource(dados);
            JasperPrint jasperPrint = JasperFillManager.fillReport(relatorioPrincipalComnpilado, params, dataSource);
            
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException("Ocorreu um problema na geração do PDF. Tente mais tarde.");
        }
    }
    
    /*
     * Metodo utilitario para montar DTO
    */
    private RelatorioDTO bindDadosRelatorio(Venda venda) {
        return RelatorioDTO.builder()
                .nomeVendedor(venda.getNomeVendedor())
                .dataVenda(Utilitarios.formatData(venda.getDataVenda()))
                .valorVenda(Utilitarios.formataValor(venda.getTotalVendaSemImposto()))
                .imposto(Utilitarios.formatPorcentagem(venda.getImposto() ))
                .produtos(venda.getProdutos().stream().map(this::bindProdutos).toList())
                .total(Utilitarios.formataValor(venda.getTotalComImposto()))
               .build();
    }
    
    /*
     * Metodo utilitario para montar DTO
    */
    private ProdutosRelatorioDTO bindProdutos(Produto produtos) {
        return ProdutosRelatorioDTO.builder()
                .codigo(produtos.getCodigo())
                .item(produtos.getItem())
                .descricao(produtos.getDescricao())
                .quantidade(produtos.getQuantidade().intValue())
                .valorUnitario(Utilitarios.formataValor(produtos.getValorUnitario()))
                .total(Utilitarios.formataValor(produtos.getTotal()))
               .build();
    }
}
