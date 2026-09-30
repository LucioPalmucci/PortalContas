package edu.usal.servlet;

import edu.usal.jdbc.dto.ComposicionCategoriaDTO;
import edu.usal.jdbc.dto.EstadoResultadosDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

final class EstadoResultadosPdf {

    static final class Datos {
        String cliente;
        EstadoResultadosDTO estado;
        List<ComposicionCategoriaDTO> desgloseGastos = new ArrayList<>();
        List<ComposicionCategoriaDTO> desgloseOtrosIngresos = new ArrayList<>();
        List<ComposicionCategoriaDTO> desgloseOtrosEgresos = new ArrayList<>();
        List<String> categoriasExcluidas = new ArrayList<>();
        String resumen;
        Date generado = new Date();
    }

    private enum Estilo { NORMAL, RUBRO, SUBTOTAL, DETALLE, FINAL }

    private static final Locale AR = Locale.forLanguageTag("es-AR");
    private static final PDFont NORMAL = PDType1Font.HELVETICA;
    private static final PDFont NEGRITA = PDType1Font.HELVETICA_BOLD;

    private static final float PAGINA_ANCHO = PDRectangle.A4.getWidth();
    private static final float PAGINA_ALTO = PDRectangle.A4.getHeight();
    private static final float MARGEN = 50f;
    private static final float ANCHO = PAGINA_ANCHO - 2 * MARGEN;
    private static final float BORDE_INFERIOR = 62f;

    // columnas de la tabla: concepto | monto | % sobre ventas
    private static final float COL_MONTO_DER = MARGEN + 395f;
    private static final float COL_PCT_DER = MARGEN + ANCHO - 8f;

    private static final Color AZUL = new Color(36, 87, 140);
    private static final Color GRIS_TEXTO = new Color(95, 105, 115);
    private static final Color GRIS_LINEA = new Color(205, 211, 218);
    private static final Color GRIS_FONDO = new Color(238, 241, 244);
    private static final Color VERDE = new Color(25, 135, 84);
    private static final Color ROJO = new Color(200, 45, 60);
    private static final Color NEGRO = new Color(30, 35, 40);

    private final PDDocument doc = new PDDocument();
    private final Datos datos;
    private final SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
    private PDPage pagina;
    private PDPageContentStream cs;
    private float y;

    private EstadoResultadosPdf(Datos datos) {
        this.datos = datos;
    }

    static void generar(OutputStream salida, Datos datos) throws IOException {
        EstadoResultadosPdf pdf = new EstadoResultadosPdf(datos);
        try {
            pdf.construir();
            pdf.pieDePagina();
            pdf.doc.save(salida);
        } finally {
            pdf.doc.close();
        }
    }

    private void construir() throws IOException {
        EstadoResultadosDTO e = datos.estado;
        double ventas = e.getVentasTotal();

        nuevaPagina(true);

        // ---- tabla principal
        encabezadoTabla();
        fila("Ventas totales", e.getVentasTotal(), ventas > 0 ? 100.0 : null, Estilo.RUBRO);
        fila("(-) Costo de mercadería vendida", e.getCmv(), porcentaje(e.getCmv(), ventas), Estilo.NORMAL);
        fila("Utilidad bruta", e.getUtilidadBruta(), porcentaje(e.getUtilidadBruta(), ventas), Estilo.SUBTOTAL);
        rubro("(-) Gastos operativos", e.getGastosTotal(), ventas, datos.desgloseGastos);
        rubro("(+) Otros ingresos", e.getoIngresosTotal(), ventas, datos.desgloseOtrosIngresos);
        rubro("(-) Otros egresos", e.getoEgresosTotal(), ventas, datos.desgloseOtrosEgresos);
        fila("Resultado neto del período", e.getGanancia(), porcentaje(e.getGanancia(), ventas), Estilo.FINAL);

        if (!datos.categoriasExcluidas.isEmpty()) {
            y -= 14;
            parrafo("Categorías excluidas del cálculo: " + String.join(", ", datos.categoriasExcluidas) + ".", NORMAL, 8.5f, GRIS_TEXTO);
        }

        y -= 14;
        titulo("Indicadores clave");
        indicadores();

        // ---- resumen
        if (datos.resumen != null && !datos.resumen.isEmpty()) {
            y -= 26;
            titulo("Resumen del período");
            parrafo(datos.resumen, NORMAL, 10f, NEGRO);
        }
    }

    private void rubro(String nombre, double total, double ventas, List<ComposicionCategoriaDTO> detalle) throws IOException {
        fila(nombre, total, porcentaje(total, ventas), Estilo.RUBRO);
        for (ComposicionCategoriaDTO c : detalle) {
            fila(c.getNombreCategoria(), c.getTotal(), porcentaje(c.getTotal(), ventas), Estilo.DETALLE);
        }
    }

    private void indicadores() throws IOException {
        EstadoResultadosDTO e = datos.estado;
        double ventas = e.getVentasTotal();
        String[] etiquetas = {"Margen de CMV utilizado", "Margen de utilidad", "Rentabilidad", "Vs. período anterior"};
        String[] valores = {
                porcentajeTexto(e.getMargenCMV()),
                ventas > 0 ? porcentajeTexto(e.getUtilidadBruta() / ventas * 100.0) : "-",
                ventas > 0 ? porcentajeTexto(e.getGanancia() / ventas * 100.0) : "-",
                (e.getVariacionAnterior() >= 0 ? "+" : "") + porcentajeTexto(e.getVariacionAnterior())
        };
        Color[] colores = {NEGRO, NEGRO, e.getGanancia() >= 0 ? VERDE : ROJO, e.getVariacionAnterior() >= 0 ? VERDE : ROJO};

        float separacion = 10f;
        float caja = (ANCHO - 3 * separacion) / 4f;
        float alto = 44f;
        asegurar(alto + 6);
        for (int i = 0; i < 4; i++) {
            float x = MARGEN + i * (caja + separacion);
            cs.setNonStrokingColor(GRIS_FONDO);
            cs.addRect(x, y - alto, caja, alto);
            cs.fill();
            cs.setNonStrokingColor(colores[i] == NEGRO ? AZUL : colores[i]);
            cs.addRect(x, y - alto, 3f, alto);
            cs.fill();
            texto(etiquetas[i], NORMAL, 8f, x + 10, y - 14, GRIS_TEXTO);
            texto(valores[i], NEGRITA, 15f, x + 10, y - 34, colores[i]);
        }
        y -= alto;
    }


    private void nuevaPagina(boolean primera) throws IOException {
        if (cs != null) {
            cs.close();
        }
        pagina = new PDPage(PDRectangle.A4);
        doc.addPage(pagina);
        cs = new PDPageContentStream(doc, pagina);
        y = PAGINA_ALTO - MARGEN;

        if (primera) {
            cs.setNonStrokingColor(AZUL);
            cs.addRect(0, PAGINA_ALTO - 10f, PAGINA_ANCHO, 10f);
            cs.fill();
            y -= 6;
            texto("Lofrano Sánchez Estudio Contable", NORMAL, 9f, MARGEN, y, GRIS_TEXTO);
            y -= 28;
            texto("Estado de resultados", NEGRITA, 22f, MARGEN, y, AZUL);
            y -= 22;
            texto("Cliente: " + valor(datos.cliente), NEGRITA, 11.5f, MARGEN, y, NEGRO);
            y -= 15;
            texto("Período: " + fecha.format(datos.estado.getPeriodoInicio()) + " al " + fecha.format(datos.estado.getPeriodoFin()),
                    NORMAL, 10.5f, MARGEN, y, NEGRO);
            y -= 14;
            linea(MARGEN, y, MARGEN + ANCHO, y, GRIS_LINEA, 0.8f);
            y -= 22;
        } else {
            texto("Estado de resultados - " + valor(datos.cliente) + " - " + fecha.format(datos.estado.getPeriodoInicio())
                    + " al " + fecha.format(datos.estado.getPeriodoFin()), NORMAL, 8.5f, MARGEN, y, GRIS_TEXTO);
            y -= 8;
            linea(MARGEN, y, MARGEN + ANCHO, y, GRIS_LINEA, 0.6f);
            y -= 18;
        }
    }

    private void asegurar(float alto) throws IOException {
        if (y - alto < BORDE_INFERIOR) {
            nuevaPagina(false);
        }
    }

    private void titulo(String texto) throws IOException {
        asegurar(28);
        texto(texto, NEGRITA, 11.5f, MARGEN, y, AZUL);
        y -= 6;
        linea(MARGEN, y, MARGEN + ANCHO, y, GRIS_LINEA, 0.6f);
        y -= 12;
    }

    private void encabezadoTabla() throws IOException {
        float alto = 20f;
        asegurar(alto + 40);
        cs.setNonStrokingColor(AZUL);
        cs.addRect(MARGEN, y - alto, ANCHO, alto);
        cs.fill();
        texto("Concepto", NEGRITA, 9.5f, MARGEN + 8, y - 13.5f, Color.WHITE);
        derecha("Monto", NEGRITA, 9.5f, COL_MONTO_DER, y - 13.5f, Color.WHITE);
        derecha("% s/ ventas", NEGRITA, 9.5f, COL_PCT_DER, y - 13.5f, Color.WHITE);
        y -= alto;
    }

    private void fila(String etiqueta, double monto, Double pct, Estilo estilo) throws IOException {
        float alto = estilo == Estilo.DETALLE ? 15f : 19f;
        asegurar(alto);
        PDFont fuente = (estilo == Estilo.NORMAL || estilo == Estilo.DETALLE) ? NORMAL : NEGRITA;
        float tam = estilo == Estilo.DETALLE ? 9f : 10f;
        float sangria = estilo == Estilo.DETALLE ? 22f : 8f;
        Color color = estilo == Estilo.DETALLE ? GRIS_TEXTO : NEGRO;

        if (estilo == Estilo.SUBTOTAL || estilo == Estilo.FINAL) {
            cs.setNonStrokingColor(GRIS_FONDO);
            cs.addRect(MARGEN, y - alto, ANCHO, alto);
            cs.fill();
        }
        float base = y - alto + (alto - tam) / 2f + 1.5f;
        String etiquetaAjustada = ajustar(etiqueta, fuente, tam, COL_MONTO_DER - MARGEN - sangria - 90f);
        texto(etiquetaAjustada, fuente, tam, MARGEN + sangria, base, color);

        Color colorMonto = estilo == Estilo.FINAL ? (monto >= 0 ? VERDE : ROJO) : color;
        derecha(moneda(monto), fuente, tam, COL_MONTO_DER, base, colorMonto);
        derecha(pct == null ? "-" : porcentajeTexto(pct), fuente, tam, COL_PCT_DER, base, estilo == Estilo.FINAL ? colorMonto : color);

        if (estilo == Estilo.FINAL) {
            linea(MARGEN, y - alto, MARGEN + ANCHO, y - alto, AZUL, 1.2f);
        } else {
            linea(MARGEN, y - alto, MARGEN + ANCHO, y - alto, GRIS_LINEA, 0.4f);
        }
        y -= alto;
    }

    private void parrafo(String contenido, PDFont fuente, float tam, Color color) throws IOException {
        for (String renglon : envolver(contenido, fuente, tam, ANCHO)) {
            asegurar(tam + 4);
            texto(renglon, fuente, tam, MARGEN, y, color);
            y -= tam + 4;
        }
    }

    private void pieDePagina() throws IOException {
        cs.close();
        cs = null;
        int total = doc.getNumberOfPages();
        String izquierda = "Contas Portal - Lofrano Sánchez Estudio Contable   |   Generado el "
                + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(datos.generado);
        for (int i = 0; i < total; i++) {
            PDPage p = doc.getPage(i);
            try (PDPageContentStream pie = new PDPageContentStream(doc, p, PDPageContentStream.AppendMode.APPEND, true, true)) {
                pie.setStrokingColor(GRIS_LINEA);
                pie.setLineWidth(0.6f);
                pie.moveTo(MARGEN, 46);
                pie.lineTo(MARGEN + ANCHO, 46);
                pie.stroke();
                escribir(pie, izquierda, NORMAL, 8f, MARGEN, 34, GRIS_TEXTO);
                String der = "Página " + (i + 1) + " de " + total;
                escribir(pie, der, NORMAL, 8f, MARGEN + ANCHO - anchoTexto(der, NORMAL, 8f), 34, GRIS_TEXTO);
            }
        }
    }

    private void texto(String t, PDFont f, float tam, float x, float yy, Color c) throws IOException {
        escribir(cs, t, f, tam, x, yy, c);
    }

    private void derecha(String t, PDFont f, float tam, float xDerecha, float yy, Color c) throws IOException {
        escribir(cs, t, f, tam, xDerecha - anchoTexto(t, f, tam), yy, c);
    }

    private static void escribir(PDPageContentStream s, String t, PDFont f, float tam, float x, float yy, Color c) throws IOException {
        s.beginText();
        s.setFont(f, tam);
        s.setNonStrokingColor(c);
        s.newLineAtOffset(x, yy);
        s.showText(limpiar(t));
        s.endText();
    }

    private void linea(float x1, float y1, float x2, float y2, Color c, float grosor) throws IOException {
        cs.setStrokingColor(c);
        cs.setLineWidth(grosor);
        cs.moveTo(x1, y1);
        cs.lineTo(x2, y2);
        cs.stroke();
    }

    private static float anchoTexto(String t, PDFont f, float tam) throws IOException {
        return f.getStringWidth(limpiar(t)) / 1000f * tam;
    }

    private static String ajustar(String t, PDFont f, float tam, float maximo) throws IOException {
        String s = limpiar(t);
        if (anchoTexto(s, f, tam) <= maximo) {
            return s;
        }
        while (s.length() > 1 && anchoTexto(s + "...", f, tam) > maximo) {
            s = s.substring(0, s.length() - 1);
        }
        return s + "...";
    }

    private static List<String> envolver(String contenido, PDFont f, float tam, float maximo) throws IOException {
        List<String> renglones = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        for (String palabra : limpiar(contenido).split(" ")) {
            String candidato = actual.length() == 0 ? palabra : actual + " " + palabra;
            if (anchoTexto(candidato, f, tam) > maximo && actual.length() > 0) {
                renglones.add(actual.toString());
                actual = new StringBuilder(palabra);
            } else {
                actual = new StringBuilder(candidato);
            }
        }
        if (actual.length() > 0) {
            renglones.add(actual.toString());
        }
        return renglones;
    }

    private static String limpiar(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(s.length());
        for (char c : s.toCharArray()) {
            if (c == '\n' || c == '\r' || c == '\t') {
                sb.append(' ');
            } else if ((c >= 32 && c <= 126) || (c >= 160 && c <= 255)) {
                sb.append(c);
            } else {
                sb.append('?');
            }
        }
        return sb.toString();
    }

    private static String valor(String s) {
        return s == null || s.trim().isEmpty() ? "-" : s.trim();
    }

    private static Double porcentaje(double monto, double ventas) {
        return ventas > 0 ? monto / ventas * 100.0 : null;
    }

    private static String moneda(double v) {
        return (v < 0 ? "-" : "") + "$ " + String.format(AR, "%,.2f", Math.abs(v));
    }

    private static String porcentajeTexto(double v) {
        return String.format(AR, "%.1f", v) + "%";
    }
}
