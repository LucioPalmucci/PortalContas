package edu.usal.servlet;

import edu.usal.jdbc.dto.ComparacionPeriodoDTO;
import edu.usal.jdbc.dto.ComposicionCategoriaDTO;
import edu.usal.jdbc.dto.EstadoFinancieroDTO;
import edu.usal.jdbc.dto.PuntoTendenciaDTO;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.ReporteServicio;
import edu.usal.jdbc.servicio.UsuarioServicio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet("/EstadoFinanciero")
public class EstadoFinancieroServlet extends HttpServlet {

    private static final String[] COLORES_SERIE = {
            "--serie-1", "--serie-2", "--serie-3", "--serie-4", "--serie-5", "--serie-6", "--serie-7", "--serie-8"
    };

    private final ReporteServicio reporteServicio;
    private final UsuarioServicio usuarioServicio;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    public EstadoFinancieroServlet() {
        this.reporteServicio = new ReporteServicio();
        this.usuarioServicio = new UsuarioServicio();
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
            if (idUsuario == null) {
                req.getRequestDispatcher("/Vistas/OpcionesUsuario/EstadoFinanciero.jsp").forward(req, resp);
                return;
            }
        }

        try {
            EstadoFinancieroDTO estadoFinanciero = reporteServicio.obtenerEstadoFinanciero(idUsuario);
            req.setAttribute("estadoFinanciero", estadoFinanciero);
            req.setAttribute("barrasGanancia", construirBarrasGanancia(estadoFinanciero.getTendenciaMensual()));
            req.setAttribute("barrasVentas", construirBarrasVentas(estadoFinanciero.getTendenciaMensual()));
            req.setAttribute("barrasComposicion", construirBarrasComposicion(estadoFinanciero.getComposicionGastos()));
            req.setAttribute("resumenEjecutivo", construirResumenEjecutivo(estadoFinanciero));

            String desde1 = req.getParameter("desde1");
            String hasta1 = req.getParameter("hasta1");
            String desde2 = req.getParameter("desde2");
            String hasta2 = req.getParameter("hasta2");
            if (desde1 != null && !desde1.isEmpty() && hasta1 != null && !hasta1.isEmpty()
                    && desde2 != null && !desde2.isEmpty() && hasta2 != null && !hasta2.isEmpty()) {
                ComparacionPeriodoDTO comparacion = reporteServicio.compararPeriodos(idUsuario,
                        formatoFecha.parse(desde1), formatoFecha.parse(hasta1),
                        formatoFecha.parse(desde2), formatoFecha.parse(hasta2));
                req.setAttribute("comparacion", comparacion);
            }
        } catch (ServiceException | java.text.ParseException e) {
            req.setAttribute("error", "No se pudo calcular el estado financiero: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesUsuario/EstadoFinanciero.jsp").forward(req, resp);
    }

    private List<PuntoBarra> construirBarrasGanancia(List<PuntoTendenciaDTO> tendencia) {
        double maxAbs = tendencia.stream().mapToDouble(p -> Math.abs(p.getGanancia())).max().orElse(1.0);
        if (maxAbs == 0) maxAbs = 1.0;
        List<PuntoBarra> barras = new ArrayList<>();
        for (PuntoTendenciaDTO p : tendencia) {
            double altura = (Math.abs(p.getGanancia()) / maxAbs) * 100.0;
            barras.add(new PuntoBarra(p.getEtiquetaMes(), altura, p.getGanancia() >= 0, formatoMoneda(p.getGanancia())));
        }
        return barras;
    }

    private List<PuntoBarra> construirBarrasVentas(List<PuntoTendenciaDTO> tendencia) {
        double max = tendencia.stream().mapToDouble(PuntoTendenciaDTO::getVentas).max().orElse(1.0);
        if (max == 0) max = 1.0;
        List<PuntoBarra> barras = new ArrayList<>();
        for (PuntoTendenciaDTO p : tendencia) {
            double altura = (p.getVentas() / max) * 100.0;
            barras.add(new PuntoBarra(p.getEtiquetaMes(), altura, true, formatoMoneda(p.getVentas())));
        }
        return barras;
    }

    // Limita la composicion a las primeras 7 categorias + un total "Otras" para no romper el orden fijo de colores categoricos.
    private List<BarraComposicion> construirBarrasComposicion(List<ComposicionCategoriaDTO> composicion) {
        List<BarraComposicion> resultado = new ArrayList<>();
        double totalOtras = 0;
        double porcentajeOtras = 0;
        for (int i = 0; i < composicion.size(); i++) {
            ComposicionCategoriaDTO c = composicion.get(i);
            if (i < 7) {
                resultado.add(new BarraComposicion(c.getNombreCategoria(), c.getPorcentaje(), formatoMoneda(c.getTotal()), COLORES_SERIE[i]));
            } else {
                totalOtras += c.getTotal();
                porcentajeOtras += c.getPorcentaje();
            }
        }
        if (totalOtras > 0) {
            resultado.add(new BarraComposicion("Otras categorias", porcentajeOtras, formatoMoneda(totalOtras), COLORES_SERIE[7]));
        }
        return resultado;
    }

    private String construirResumenEjecutivo(EstadoFinancieroDTO estadoFinanciero) {
        List<PuntoTendenciaDTO> tendencia = estadoFinanciero.getTendenciaMensual();
        StringBuilder texto = new StringBuilder();

        if (estadoFinanciero.getMesesPerdida() == 0) {
            texto.append("En los ultimos 12 meses el negocio no tuvo meses con perdida. ");
        } else {
            texto.append("En los ultimos 12 meses el negocio tuvo perdida en ")
                    .append(estadoFinanciero.getMesesPerdida())
                    .append(estadoFinanciero.getMesesPerdida() == 1 ? " mes. " : " meses. ");
        }
        texto.append("El mejor mes fue ").append(estadoFinanciero.getMejorMes())
                .append(" y el mas dificil fue ").append(estadoFinanciero.getPeorMes()).append(". ");

        if (!tendencia.isEmpty()) {
            PuntoTendenciaDTO ultimo = tendencia.get(tendencia.size() - 1);
            String resultado = ultimo.getGanancia() >= 0 ? "una ganancia" : "una perdida";
            texto.append("En ").append(ultimo.getEtiquetaMes()).append(" (el mes mas reciente), el negocio registro ")
                    .append(resultado).append(" de ").append(formatoMoneda(Math.abs(ultimo.getGanancia())))
                    .append(", con una rentabilidad del ").append(String.format(Locale.forLanguageTag("es-AR"), "%.1f", ultimo.getRentabilidad())).append("%.");
        }
        return texto.toString();
    }

    private String formatoMoneda(double valor) {
        return String.format(Locale.forLanguageTag("es-AR"), "$ %,.2f", valor);
    }
}
