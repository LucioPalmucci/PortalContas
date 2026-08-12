package edu.usal.servlet;

import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.dominio.OtroEgreso;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.CategoriaConceptoServicio;
import edu.usal.jdbc.servicio.MetodoOperacionServicio;
import edu.usal.jdbc.servicio.OtroEgresoServicio;
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

@WebServlet("/OtroEgreso")
public class OtroEgresoServlet extends HttpServlet {

    private final OtroEgresoServicio otroEgresoServicio;
    private final MetodoOperacionServicio metodoOperacionServicio;
    private final CategoriaConceptoServicio categoriaConceptoServicio;
    private final UsuarioServicio usuarioServicio;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    public OtroEgresoServlet() {
        this.otroEgresoServicio = new OtroEgresoServicio();
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
            List<OtroEgreso> otrosEgresos;
            if (esAdmin) {
                req.setAttribute("clientes", usuarioServicio.obtenerTodosLosUsuarios());
                String idUsuarioFiltro = req.getParameter("idUsuarioFiltro");
                otrosEgresos = (idUsuarioFiltro != null && !idUsuarioFiltro.isEmpty())
                        ? otroEgresoServicio.obtenerOtrosEgresosPorUsuario(Integer.parseInt(idUsuarioFiltro))
                        : otroEgresoServicio.obtenerTodosLosOtrosEgresos();
                req.setAttribute("idUsuarioFiltro", idUsuarioFiltro);
            } else {
                otrosEgresos = otroEgresoServicio.obtenerOtrosEgresosPorUsuario(idUsuario);
            }
            req.setAttribute("otrosEgresos", otrosEgresos);
            req.setAttribute("esAdmin", esAdmin);
            req.setAttribute("metodosPago", metodoOperacionServicio.obtenerMetodosPorTipo("PAGO"));
            req.setAttribute("categorias", categoriaConceptoServicio.obtenerCategoriasPorAplicaA("EGRESO"));

            List<OtroEgreso> otrosEgresosActivos = new java.util.ArrayList<>();
            for (OtroEgreso oe : otrosEgresos) {
                if (oe.getEstado() != EstadoPago.ANULADO) otrosEgresosActivos.add(oe);
            }
            req.setAttribute("evolucionMensual", EvolucionMensualUtil.calcularUltimos12Meses(otrosEgresosActivos, OtroEgreso::getFecha, OtroEgreso::getValor));

            String editarId = req.getParameter("editarId");
            if (editarId != null && !editarId.isEmpty()) {
                OtroEgreso otroEgreso = otroEgresoServicio.obtenerOtroEgresoPorId(Integer.parseInt(editarId));
                if (otroEgreso != null && (esAdmin || otroEgreso.getUsuario().getIdUsuario() == idUsuario)) {
                    req.setAttribute("otroEgresoAEditar", otroEgreso);
                }
            }
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudieron cargar los otros egresos: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesUsuario/OtrosEgresos.jsp").forward(req, resp);
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
                agregarOtroEgreso(req, idUsuario, esAdmin(req));
            } else if ("editar".equals(action)) {
                editarOtroEgreso(req);
            } else if ("marcarPagado".equals(action)) {
                otroEgresoServicio.cambiarEstadoOtroEgreso(Integer.parseInt(req.getParameter("idOtroEgreso")), EstadoPago.PAGADO.name());
                req.setAttribute("exito", "El otro egreso fue marcado como pagado.");
            } else if ("anular".equals(action)) {
                otroEgresoServicio.cambiarEstadoOtroEgreso(Integer.parseInt(req.getParameter("idOtroEgreso")), EstadoPago.ANULADO.name());
                req.setAttribute("exito", "El otro egreso fue anulado.");
            }
        } catch (ServiceException | NumberFormatException | ParseException e) {
            req.setAttribute("error", "No se pudo completar la operacion: " + e.getMessage());
        }
        doGet(req, resp);
    }

    private void agregarOtroEgreso(HttpServletRequest req, int idUsuarioSesion, boolean esAdmin) throws ParseException {
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

        Date fechaOtroEgreso = formatoFecha.parse(fecha);
        boolean exito = otroEgresoServicio.crearOtroEgreso(idUsuario, fechaOtroEgreso, descripcion, Integer.parseInt(idCategoria),
                Double.parseDouble(valor), Integer.parseInt(idMetodo),
                estado == null || estado.isEmpty() ? EstadoPago.PENDIENTE_DE_PAGO.name() : estado);

        req.setAttribute(exito ? "exito" : "error", exito ? "Otro egreso registrado correctamente." : "No se pudo registrar el otro egreso.");
    }

    private void editarOtroEgreso(HttpServletRequest req) {
        String idOtroEgreso = req.getParameter("idOtroEgreso");
        String idCategoria = req.getParameter("idCategoria");
        String valor = req.getParameter("valor");
        String idMetodo = req.getParameter("idMetodo");
        String descripcion = req.getParameter("descripcion");
        String estado = req.getParameter("estado");

        if (idOtroEgreso == null || idOtroEgreso.isEmpty()) {
            req.setAttribute("error", "Falta el identificador del otro egreso.");
            return;
        }

        boolean exito = otroEgresoServicio.editarOtroEgreso(Integer.parseInt(idOtroEgreso), descripcion, Integer.parseInt(idCategoria),
                Double.parseDouble(valor), Integer.parseInt(idMetodo), estado);

        req.setAttribute(exito ? "exito" : "error", exito ? "Otro egreso actualizado correctamente." : "No se pudo actualizar el otro egreso.");
    }
}
