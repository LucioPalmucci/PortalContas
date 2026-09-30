package edu.usal.jdbc.util;

import java.math.BigDecimal;

public final class ValidacionMovimientoUtil {

    private ValidacionMovimientoUtil() {}

    public static String validarMonto(String texto, String campo) {
        try {
            BigDecimal monto = new BigDecimal(texto.trim());
            if (monto.signum() <= 0) return "El " + campo + " debe ser mayor a cero.";
            if (monto.stripTrailingZeros().scale() > 2) return "El " + campo + " admite hasta dos decimales.";
            return null;
        } catch (NumberFormatException | NullPointerException e) {
            return "El " + campo + " no es un numero valido.";
        }
    }

    public static String validarCantidad(String texto) {
        try {
            return Integer.parseInt(texto.trim()) >= 1 ? null : "La cantidad debe ser un entero mayor o igual a 1.";
        } catch (NumberFormatException | NullPointerException e) {
            return "La cantidad debe ser un entero mayor o igual a 1.";
        }
    }
}
