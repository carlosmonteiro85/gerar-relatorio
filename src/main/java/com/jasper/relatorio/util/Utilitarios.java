package com.jasper.relatorio.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class Utilitarios {

    public static String formatData(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }

    public static String formataValor(final BigDecimal valor) {
        final DecimalFormat decimalFormatReal = new DecimalFormat("¤ ###,###,##0.00",
                new DecimalFormatSymbols(new Locale("pt", "BR")));
        return Objects.nonNull(valor) ? decimalFormatReal.format(valor) : decimalFormatReal.format(BigDecimal.ZERO);
    }

    public static String formatPorcentagem(BigDecimal valor) {
        DecimalFormat df = new DecimalFormat("0.##");
        String valorFormatado = df.format(valor);
        return valorFormatado + "%";
    }
}
