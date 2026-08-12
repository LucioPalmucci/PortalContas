package edu.usal.servlet;

import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.dominio.Gasto;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.CategoriaConceptoServicio;
import edu.usal.jdbc.servicio.GastoServicio;
import edu.usal.jdbc.servicio.MetodoOperacionServicio;
import edu.usal.jdbc.servicio.UsuarioServicio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/Gasto")
public class GastoServlet extends HttpServlet {

    private final GastoServicio gastoServicio;
    private final MetodoOperacionServicio metodoOperacionServicio;
    private final CategoriaConceptoServicio categoriaConceptoServicio;
    private final UsuarioServicio usuarioServicio;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    public GastoServlet() {
        this.gastoServicio = new GastoServicio();
        this.metodoOperacionServicio = new MetodoOperacionServicio();
        this.categoriaConceptoServicio = new CategoriaConceptoServicio();
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
        int idUsuario = (Integer) idUsuarioAttr;
        boolean esAdmin = esAdmin(req);
        try {
            List<Gasto> gastos;
            if (esAdmin) {
                req.setAttribute("clientes", usuarioServicio.obtenerTodosLosUsuarios());
                String idUsuarioFiltro = req.getParameter("idUsuarioFiltro");
                gastos = (idUsuarioFiltro != null && !idUsuarioFiltro.isEmpty())
                        ? gastoServicio.obtenerGastosPorUsuario(Integer.parseInt(idUsuarioFiltro))
                        : gastoServicio.obtenerTodosLosGastos();
                req.setAttribute("idUsuarioFiltro", idUsuarioFiltro);
            } else {
                gastos = gastoServicio.obtenerGastosPorUsuario(idUsuario);
            }
            req.setAttribute("gastos", gastos);
            req.setAttribute("esAdmin", esAdmin);
            req.setAttribute("metodosPago", metodoOperacionServicio.obtenerMetodosPorTipo("PAGO"));
            req.setAttribute("categorias", categoriaConceptoServicio.obtenerCategoriasPorAplicaA("GASTO"));

            List<Gasto> gastosActivos = new java.util.ArrayList<>();
            for (Gasto g : gastos) {
                if (g.getEstado() != EstadoPago.ANULADO) gastosActivos.add(g);
            }
            req.setAttribute("evolucionMensual", EvolucionMensualUtil.calcularUltimos12Meses(gastosActivos, Gasto::getFecha, Gasto::getValor));

            String editarId = req.getParameter("editarId");
            if (editarId != null && !editarId.isEmpty()) {
                Gasto gasto = gastoServicio.obtenerGastoPorId(Integer.parseInt(editarId));
                if (gasto != null && (esAdmin || gasto.getUsuario().getIdUsuario() == idUsuario)) {
                    req.setAttribute("gastoAEditar", gasto);
                }
            }
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudieron cargar los gastos: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesUsuario/Gastos.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object idUsuarioAttr = req.getSession().getAttribute("idUsuario");
        if (idUsuarioAttr == null) {
            resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
            return;
        }
        int idUsuario = (Integer) idUsuarioAttr;
        String action = req.getParameter("action");

        try {
            if ("agregar".equals(action)) {
                agregarGasto(req, idUsuario, esAdmin(req));
            } else if ("editar".equals(action)) {
                editarGasto(req);
            } else if ("marcarPagado".equals(action)) {
                gastoServicio.cambiarEstadoGasto(Integer.parseInt(req.getParameter("idGasto")), EstadoPago.PAGADO.name());
                req.setAttribute("exito", "El gasto fue marcado como pagado.");
            } else if ("anular".equals(action)) {
                gastoServicio.cambiarEstadoGasto(Integer.parseInt(req.getParameter("idGasto")), EstadoPago.ANULADO.name());
                req.setAttribute("exito", "El gasto fue anulado.");
            }
        } catch (ServiceException | NumberFormatException | ParseException e) {
            req.setAttribute("error", "No se pudo completar la operacion: " + e.getMessage());
        }
        doGet(req, resp);
    }

    private void agregarGasto(HttpServletRequest req, int idUsuarioSesion, boolean esAdmin) throws ParseException {
        String fecha = req.getParameter("fecha");
        String idCategoria = req.getParameter("idCategoria");
        String valor = req.getParameter("valor");
        String idMetodo = req.getParameter("idMetodo");
        String descripcion = req.getParameter("descripcion");
        String estado = req.getParameter("estado");
        String idUsuarioDestino = req.getParameter("idUsuarioDestino");

        if (fecha == null || fecha.isEmpty() || idCategoria == null || idCategoria.isEmpty()
                || valor == null || valor.isEmpty() || idMetodo == null || idMetodo.isEmpty()
                || (esAdmin && (idUsuarioDestino == null || idUsuarioDestino.isEmpty()))) {
            req.setAttribute("error", "Complete todos los campos obligatorios.");
            return;
        }

        int idUsuario = (esAdmin && idUsuarioDestino != null && !idUsuarioDestino.isEmpty())
                ? Integer.parseInt(idUsuarioDestino) : idUsuarioSesion;

        Date fechaGasto = formatoFecha.parse(fecha);
        boolean exito = gastoServicio.crearGasto(idUsuario, fechaGasto, descripcion, Integer.parseInt(idCategoria),
                Double.parseDouble(valor), Integer.parseInt(idMetodo),
                estado == null || estado.isEmpty() ? EstadoPago.PENDIENTE_DE_PAGO.name() : estado);

        req.setAttribute(exito ? "exito" : "error", exito ? "Gasto registrado correctamente." : "No se pudo registrar el gasto.");
    }

    private void editarGasto(HttpServletRequest req) {
        String idGasto = req.getParameter("idGasto");
        String idCategoria = req.getParameter("idCategoria");
        String valor = req.getParameter("valor");
        String idMetodo = req.getParameter("idMetodo");
        String descripcion = req.getParameter("descripcion");
        String estado = req.getParameter("estado");

        if (idGasto == null || idGasto.isEmpty()) {
            req.setAttribute("error", "Falta el identificador del gasto.");
            return;
        }

        boolean exito = gastoServicio.editarGasto(Integer.parseInt(idGasto), descripcion, Integer.parseInt(idCategoria),
                Double.parseDouble(valor), Integer.parseInt(idMetodo), estado);

        req.setAttribute(exito ? "exito" : "error", exito ? "Gasto actualizado correctamente." : "No se pudo actualizar el gasto.");
    }
}
