package edu.usal.servlet;

import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.dominio.Compra;
import edu.usal.jdbc.dominio.ConfiguracionEstadoResultados;
import edu.usal.jdbc.dominio.Gasto;
import edu.usal.jdbc.dominio.OtroEgreso;
import edu.usal.jdbc.dominio.OtroIngreso;
import edu.usal.jdbc.dominio.Venta;
import edu.usal.jdbc.dto.EstadoResultadosDTO;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.CategoriaConceptoServicio;
import edu.usal.jdbc.servicio.CompraServicio;
import edu.usal.jdbc.servicio.ConfiguracionServicio;
import edu.usal.jdbc.servicio.GastoServicio;
import edu.usal.jdbc.servicio.OtroEgresoServicio;
import edu.usal.jdbc.servicio.OtroIngresoServicio;
import edu.usal.jdbc.servicio.ReporteServicio;
import edu.usal.jdbc.servicio.UsuarioServicio;
import edu.usal.jdbc.servicio.VentaServicio;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@WebServlet("/EstadoResultados")
public class EstadoResultadosServlet extends HttpServlet {

    private static final String[] COLORES_SERIE = {
            "#4E79A7", "#F28E2B", "#E15759", "#76B7B2", "#59A14F", "#EDC948", "#B07AA1", "#9C9C9C"
    };

    private final ReporteServicio reporteServicio;
    private final ConfiguracionServicio configuracionServicio;
    private final CategoriaConceptoServicio categoriaConceptoServicio;
    private final UsuarioServicio usuarioServicio;
    private final VentaServicio ventaServicio;
    private final CompraServicio compraServicio;
    private final GastoServicio gastoServicio;
    private final OtroIngresoServicio otroIngresoServicio;
    private final OtroEgresoServicio otroEgresoServicio;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat formatoFechaMostrar = new SimpleDateFormat("dd/MM/yyyy");

    public EstadoResultadosServlet() {
        this.reporteServicio = new ReporteServicio();
        this.configuracionServicio = new ConfiguracionServicio();
        this.categoriaConceptoServicio = new CategoriaConceptoServicio();
        this.usuarioServicio = new UsuarioServicio();
        this.ventaServicio = new VentaServicio();
        this.compraServicio = new CompraServicio();
        this.gastoServicio = new GastoServicio();
        this.otroIngresoServicio = new OtroIngresoServicio();
        this.otroEgresoServicio = new OtroEgresoServicio();
    }

    private boolean esAdmin(HttpServletRequest req) {
        return "ADMINISTRADOR".equals(req.getSession().getAttribute("rolUsuario"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object idUsuarioAttr = req.getSession().getAttribute("idUsuario");
        if (idUsuarioAttr == null) {
            resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
            return;
        }
        boolean esAdmin = esAdmin(req);
        req.setAttribute("esAdmin", esAdmin);
        Integer idUsuario = (Integer) idUsuarioAttr;

        if (esAdmin) {
            try {
                req.setAttribute("clientes", usuarioServicio.obtenerTodosLosUsuarios());
            } catch (ServiceException e) {
                req.setAttribute("error", "No se pudieron cargar los clientes: " + e.getMessage());
            }
            String idUsuarioFiltro = req.getParameter("idUsuarioFiltro");
            req.setAttribute("idUsuarioFiltro", idUsuarioFiltro);
            idUsuario = (idUsuarioFiltro != null && !idUsuarioFiltro.isEmpty()) ? Integer.parseInt(idUsuarioFiltro) : null;
        }

        if ("exportarPdf".equals(req.getParameter("action"))) {
            if (idUsuario == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Seleccione un cliente antes de exportar.");
                return;
            }
            exportarPdf(req, resp, idUsuario);
            return;
        }

        if (esAdmin && idUsuario == null) {
            req.getRequestDispatcher("/Vistas/OpcionesUsuario/EstadoResultados.jsp").forward(req, resp);
            return;
        }

        if ("volver".equals(req.getParameter("action"))) {
            req.getSession().removeAttribute("estadoResultadosGenerado");
        }
        req.setAttribute("mostrarResultados", req.getSession().getAttribute("estadoResultadosGenerado") != null);

        try {
            ConfiguracionEstadoResultados config = configuracionServicio.obtenerOCrearConfiguracion(idUsuario);
            req.setAttribute("configuracion", config);
            req.setAttribute("categorias", categoriaConceptoServicio.obtenerTodasLasCategorias());

            List<Integer> idsExcluidasActuales = new ArrayList<>();
            if (config.getCategoriasExcluidas() != null) {
                for (CategoriaConcepto c : config.getCategoriasExcluidas()) idsExcluidasActuales.add(c.getIdCategoria());
            }
            req.setAttribute("idsExcluidasActuales", idsExcluidasActuales);

            Date desde = obtenerFecha(req.getParameter("desde"), config.getPeriodoInicioDefault());
            Date hasta = obtenerFecha(req.getParameter("hasta"), config.getPeriodoFinDefault());
            req.setAttribute("desde", formatoFecha.format(desde));
            req.setAttribute("hasta", formatoFecha.format(hasta));

            EstadoResultadosDTO estado = reporteServicio.generarEstadoResultados(idUsuario, desde, hasta);
            req.setAttribute("estado", estado);

            Set<Integer> idsExcluidasSet = new java.util.HashSet<>(idsExcluidasActuales);
            List<MovimientoResumen> incluidos = new ArrayList<>();
            List<MovimientoResumen> excluidos = new ArrayList<>();

            for (Venta v : ventaServicio.obtenerVentasPorUsuarioYPeriodo(idUsuario, desde, hasta)) {
                incluidos.add(new MovimientoResumen("Venta", v.getFecha(), v.getConceptoVenta(), v.getSubtotal()));
            }
            for (Compra c : compraServicio.obtenerComprasPorUsuarioYPeriodo(idUsuario, desde, hasta)) {
                incluidos.add(new MovimientoResumen("Compra", c.getFecha(), c.getConceptoCompra(), c.getValorTotal()));
            }
            for (Gasto g : gastoServicio.obtenerGastosPorUsuarioYPeriodo(idUsuario, desde, hasta)) {
                MovimientoResumen fila = new MovimientoResumen("Gasto", g.getFecha(), g.getCategoria().getNombre(), g.getValor());
                (idsExcluidasSet.contains(g.getCategoria().getIdCategoria()) ? excluidos : incluidos).add(fila);
            }
            for (OtroIngreso oi : otroIngresoServicio.obtenerOtrosIngresosPorUsuarioYPeriodo(idUsuario, desde, hasta)) {
                MovimientoResumen fila = new MovimientoResumen("Otro ingreso", oi.getFecha(), oi.getCategoria().getNombre(), oi.getValor());
                (idsExcluidasSet.contains(oi.getCategoria().getIdCategoria()) ? excluidos : incluidos).add(fila);
            }
            for (OtroEgreso oe : otroEgresoServicio.obtenerOtrosEgresosPorUsuarioYPeriodo(idUsuario, desde, hasta)) {
                MovimientoResumen fila = new MovimientoResumen("Otro egreso", oe.getFecha(), oe.getCategoria().getNombre(), oe.getValor());
                (idsExcluidasSet.contains(oe.getCategoria().getIdCategoria()) ? excluidos : incluidos).add(fila);
            }
            req.setAttribute("movimientosIncluidos", incluidos);
            req.setAttribute("movimientosExcluidos", excluidos);

            req.setAttribute("composicionFinanciera", construirComposicionFinanciera(estado));

            req.setAttribute("resumenNarrativo", construirResumenNarrativo(estado));
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudo generar el estado de resultados: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesUsuario/EstadoResultados.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object idUsuarioAttr = req.getSession().getAttribute("idUsuario");
        if (idUsuarioAttr == null) {
            resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
            return;
        }
        int idUsuario = (Integer) idUsuarioAttr;
        if (esAdmin(req)) {
            String idUsuarioFiltro = req.getParameter("idUsuarioFiltro");
            if (idUsuarioFiltro != null && !idUsuarioFiltro.isEmpty()) {
                idUsuario = Integer.parseInt(idUsuarioFiltro);
            }
        }
        String action = req.getParameter("action");

        try {
            if ("guardarConfiguracion".equals(action)) {
                Date periodoInicio = formatoFecha.parse(req.getParameter("periodoInicioDefault"));
                Date periodoFin = formatoFecha.parse(req.getParameter("periodoFinDefault"));
                double margenCMV = Double.parseDouble(req.getParameter("margenCMV"));
                String[] idsSeleccionados = req.getParameterValues("categoriasExcluidas");
                List<Integer> idsCategorias = new ArrayList<>();
                if (idsSeleccionados != null) {
                    for (String id : idsSeleccionados) idsCategorias.add(Integer.parseInt(id));
                }
                configuracionServicio.guardarOActualizarConfiguracion(idUsuario, periodoInicio, periodoFin, margenCMV, idsCategorias);
                req.getSession().setAttribute("estadoResultadosGenerado", true);
            }
        } catch (ServiceException | NumberFormatException | java.text.ParseException e) {
            req.setAttribute("error", "No se pudo guardar la configuracion: " + e.getMessage());
        }
        doGet(req, resp);
    }

    private void exportarPdf(HttpServletRequest req, HttpServletResponse resp, int idUsuario) throws IOException {
        try {
            ConfiguracionEstadoResultados config = configuracionServicio.obtenerOCrearConfiguracion(idUsuario);
            Date desde = obtenerFecha(req.getParameter("desde"), config.getPeriodoInicioDefault());
            Date hasta = obtenerFecha(req.getParameter("hasta"), config.getPeriodoFinDefault());
            EstadoResultadosDTO estado = reporteServicio.generarEstadoResultados(idUsuario, desde, hasta);

            try (PDDocument documento = new PDDocument()) {
                PDPage pagina = new PDPage(PDRectangle.A4);
                documento.addPage(pagina);

                try (PDPageContentStream cs = new PDPageContentStream(documento, pagina)) {
                    float y = 780;
                    y = escribirLinea(cs, PDType1Font.HELVETICA_BOLD, 18, 50, y, "Estado de resultados");
                    y -= 8;
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 11, 50, y,
                            "Periodo: " + formatoFechaMostrar.format(estado.getPeriodoInicio()) + " al " + formatoFechaMostrar.format(estado.getPeriodoFin()));
                    y -= 14;
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 12, 50, y, "Ventas totales: " + formatoMoneda(estado.getVentasTotal()));
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 12, 50, y, "Costo de mercaderia vendida: " + formatoMoneda(estado.getCmv()));
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 12, 50, y, "Utilidad bruta: " + formatoMoneda(estado.getUtilidadBruta()));
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 12, 50, y, "Gastos operativos: " + formatoMoneda(estado.getGastosTotal()));
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 12, 50, y, "Otros ingresos: " + formatoMoneda(estado.getoIngresosTotal()));
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 12, 50, y, "Otros egresos: " + formatoMoneda(estado.getoEgresosTotal()));
                    y -= 6;
                    y = escribirLinea(cs, PDType1Font.HELVETICA_BOLD, 13, 50, y, "Ganancia del periodo: " + formatoMoneda(estado.getGanancia()));
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 12, 50, y, "Rentabilidad: " + formatoPorcentaje(estado.getRentabilidad()));
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 12, 50, y, "Margen de utilidad: " + formatoPorcentaje(estado.getMargenUtilidad()));
                    y = escribirLinea(cs, PDType1Font.HELVETICA, 12, 50, y,
                            "Variacion vs. periodo anterior: " + (estado.getVariacionAnterior() >= 0 ? "+" : "") + formatoPorcentaje(estado.getVariacionAnterior()));
                    y -= 20;
                    escribirLinea(cs, PDType1Font.HELVETICA_OBLIQUE, 9, 50, y,
                            "Generado el " + formatoFechaMostrar.format(new Date()) + " - Contas Portal - Lofrano Sanchez Estudio Contable");
                }

                resp.setContentType("application/pdf");
                resp.setHeader("Content-Disposition", "attachment; filename=\"estado-resultados.pdf\"");
                try (OutputStream salida = resp.getOutputStream()) {
                    documento.save(salida);
                }
            }
        } catch (ServiceException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo generar el PDF: " + e.getMessage());
        }
    }

    // Composicion financiera: distribucion de ingresos y egresos por categoria general del estado de resultados.
    private List<BarraComposicion> construirComposicionFinanciera(EstadoResultadosDTO estado) {
        List<BarraComposicion> resultado = new ArrayList<>();
        String[] nombres = {"Ventas", "Costo de mercaderia vendida", "Gastos operativos", "Otros ingresos", "Otros egresos"};
        double[] valores = {estado.getVentasTotal(), estado.getCmv(), estado.getGastosTotal(), estado.getoIngresosTotal(), estado.getoEgresosTotal()};

        double total = 0;
        for (double v : valores) total += Math.abs(v);

        for (int i = 0; i < nombres.length; i++) {
            double porcentaje = total > 0 ? (Math.abs(valores[i]) / total) * 100.0 : 0.0;
            resultado.add(new BarraComposicion(nombres[i], porcentaje, formatoMoneda(valores[i]), COLORES_SERIE[i]));
        }
        return resultado;
    }

    private String construirResumenNarrativo(EstadoResultadosDTO estado) {
        String tendenciaGanancia = estado.getVariacionAnterior() > 0 ? "aumento"
                : estado.getVariacionAnterior() < 0 ? "disminucion" : "se mantuvo estable";
        String resultado = estado.getGanancia() >= 0 ? "una ganancia" : "una perdida";

        StringBuilder texto = new StringBuilder();
        texto.append("En el periodo analizado, el negocio registro ").append(resultado)
                .append(" de ").append(formatoMoneda(Math.abs(estado.getGanancia())))
                .append(", equivalente a una rentabilidad del ").append(formatoPorcentaje(estado.getRentabilidad())).append(". ");

        if (estado.getVariacionAnterior() == 0) {
            texto.append("La ganancia se mantuvo estable respecto al periodo anterior.");
        } else {
            texto.append("La ganancia tuvo un ").append(tendenciaGanancia)
                    .append(" del ").append(formatoPorcentaje(Math.abs(estado.getVariacionAnterior())))
                    .append(" respecto al periodo inmediato anterior.");
        }
        return texto.toString();
    }

    private float escribirLinea(PDPageContentStream cs, PDFont fuente, float tamanio, float x, float y, String texto) throws IOException {
        cs.beginText();
        cs.setFont(fuente, tamanio);
        cs.newLineAtOffset(x, y);
        cs.showText(texto);
        cs.endText();
        return y - (tamanio + 6);
    }

    private String formatoMoneda(double valor) {
        return "$" + String.format(Locale.US, "%,.2f", valor);
    }

    private String formatoPorcentaje(double valor) {
        return String.format(Locale.US, "%.1f%%", valor);
    }

    private Date obtenerFecha(String parametro, Date porDefecto) throws ParseFechaException {
        if (parametro == null || parametro.isEmpty()) return porDefecto;
        try {
            return formatoFecha.parse(parametro);
        } catch (java.text.ParseException e) {
            throw new ParseFechaException("Fecha invalida");
        }
    }

    private static class ParseFechaException extends ServiceException {
        ParseFechaException(String mensaje) { super(mensaje); }
    }
}
