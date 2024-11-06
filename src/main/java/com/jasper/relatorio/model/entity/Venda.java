package com.jasper.relatorio.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Venda {
    private String nomeVendedor;
    private LocalDate dataVenda;
    private List<Produto> produtos;
    private BigDecimal imposto = new BigDecimal(0.07);

    public BigDecimal getTotalVendaSemImposto() {
        return produtos.stream()
                .map(Produto::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalComImposto() {
        BigDecimal impostoPorcen = getTotalVendaSemImposto().multiply(this.imposto);
        return getTotalVendaSemImposto().subtract(impostoPorcen);
    }

    public static Venda buscarVenda(){
        Venda venda = new Venda();
        venda.setDataVenda(LocalDate.now());
        venda.setNomeVendedor("Vendedor da Silva");
        List<Produto> produtos = List.of(
                Produto.builder()
                        .codigo("A123")
                        .item("Pc gamer")
                        .Descricao("i5 processador, 500 GB")
                        .quantidade(new BigDecimal("3"))
                        .valorUnitario(new BigDecimal("1003.45"))
                    .build(),
                    Produto.builder()
                        .codigo("A124")
                        .item("Teclado")
                        .Descricao("Teclado mecânico")
                        .quantidade(new BigDecimal("1"))
                        .valorUnitario(new BigDecimal("46.59"))
                    .build(),
                    Produto.builder()
                        .codigo("A125")
                        .item("Mouse")
                        .Descricao("Mouse O´ptico")
                        .quantidade(new BigDecimal("2"))
                        .valorUnitario(new BigDecimal("34.70"))
                    .build()
        );
        venda.setProdutos(produtos);
        return venda;
    }
}
