package com.jasper.relatorio.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RelatorioDTO {
    private String nomeVendedor;
    private String dataVenda;
    private String valorVenda;
    private String imposto;
    private String total;
    private List<ProdutosRelatorioDTO> produtos;
}
