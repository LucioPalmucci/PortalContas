package edu.usal.servlet;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

// Agrupa una lista de movimientos por mes (ultimos 12 meses) para alimentar un grafico de barras en CSS puro.
public final class EvolucionMensualUtil {

    private static final Locale LOCALE_ES = new Locale("es", "ES");

    private EvolucionMensualUtil() {}

    public static <T> List<PuntoBarra> calcularUltimos12Meses(List<T> registros, Function<T, Date> obtenerFecha, ToDoubleFunction<T> obtenerValor) {
        YearMonth mesActual = YearMonth.now();
        Map<YearMonth, Double> totales = new LinkedHashMap<>();
        for (int i = 11; i >= 0; i--) {
            totales.put(mesActual.minusMonths(i), 0.0);
        }
        for (T registro : registros) {
            Date fecha = obtenerFecha.apply(registro);
            if (fecha == null) continue;
            LocalDate fechaLocal = new java.sql.Date(fecha.getTime()).toLocalDate();
            YearMonth mes = YearMonth.from(fechaLocal);
            if (totales.containsKey(mes)) {
                totales.merge(mes, obtenerValor.applyAsDouble(registro), Double::sum);
            }
        }

        double max = totales.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
        double maxFinal = max == 0 ? 1.0 : max;

        List<PuntoBarra> barras = new ArrayList<>();
        for (Map.Entry<YearMonth, Double> entry : totales.entrySet()) {
            String etiqueta = capitalizar(entry.getKey().getMonth().getDisplayName(TextStyle.SHORT, LOCALE_ES));
            double altura = (entry.getValue() / maxFinal) * 100.0;
            barras.add(new PuntoBarra(etiqueta, altura, true, formatoMoneda(entry.getValue())));
        }
        return barras;
    }

    private static String capitalizar(String texto) {
        return (texto == null || texto.isEmpty()) ? texto : Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    private static String formatoMoneda(double valor) {
        return String.format(Locale.forLanguageTag("es-AR"), "$ %,.2f", valor);
    }
}
