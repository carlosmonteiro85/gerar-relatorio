package com.jasper.relatorio.model.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
    private String codigo;
    private String item;
    private String Descricao;
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;

    public BigDecimal getTotal() {
        return valorUnitario.multiply(quantidade);
    }
}
