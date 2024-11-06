package com.jasper.relatorio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutosRelatorioDTO {
    private String codigo;
    private String item;
    private String descricao;
    private Integer quantidade;
    private String valorUnitario;
    private String total; 
}
