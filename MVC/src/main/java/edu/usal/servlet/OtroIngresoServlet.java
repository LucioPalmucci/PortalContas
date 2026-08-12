package edu.usal.servlet;

import edu.usal.jdbc.dominio.EstadoCobro;
import edu.usal.jdbc.dominio.OtroIngreso;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.CategoriaConceptoServicio;
import edu.usal.jdbc.servicio.MetodoOperacionServicio;
import edu.usal.jdbc.servicio.OtroIngresoServicio;
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

@WebServlet("/OtroIngreso")
public class OtroIngresoServlet extends HttpServlet {

    private final OtroIngresoServicio otroIngresoServicio;
    private final MetodoOperacionServicio metodoOperacionServicio;
    private final CategoriaConceptoServicio categoriaConceptoServicio;
    private final UsuarioServicio usuarioServicio;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    public OtroIngresoServlet() {
        this.otroIngresoServicio = new OtroIngresoServicio();
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
            List<OtroIngreso> otrosIngresos;
            if (esAdmin) {
                req.setAttribute("clientes", usuarioServicio.obtenerTodosLosUsuarios());
                String idUsuarioFiltro = req.getParameter("idUsuarioFiltro");
                otrosIngresos = (idUsuarioFiltro != null && !idUsuarioFiltro.isEmpty())
                        ? otroIngresoServicio.obtenerOtrosIngresosPorUsuario(Integer.parseInt(idUsuarioFiltro))
                        : otroIngresoServicio.obtenerTodosLosOtrosIngresos();
                req.setAttribute("idUsuarioFiltro", idUsuarioFiltro);
            } else {
                otrosIngresos = otroIngresoServicio.obtenerOtrosIngresosPorUsuario(idUsuario);
            }
            req.setAttribute("otrosIngresos", otrosIngresos);
            req.setAttribute("esAdmin", esAdmin);
            req.setAttribute("metodosCobro", metodoOperacionServicio.obtenerMetodosPorTipo("COBRO"));
            req.setAttribute("categorias", categoriaConceptoServicio.obtenerCategoriasPorAplicaA("INGRESO"));

            List<OtroIngreso> otrosIngresosActivos = new java.util.ArrayList<>();
            for (OtroIngreso oi : otrosIngresos) {
                if (oi.getEstado() != EstadoCobro.ANULADO) otrosIngresosActivos.add(oi);
            }
            req.setAttribute("evolucionMensual", EvolucionMensualUtil.calcularUltimos12Meses(otrosIngresosActivos, OtroIngreso::getFecha, OtroIngreso::getValor));

            String editarId = req.getParameter("editarId");
            if (editarId != null && !editarId.isEmpty()) {
                OtroIngreso otroIngreso = otroIngresoServicio.obtenerOtroIngresoPorId(Integer.parseInt(editarId));
                if (otroIngreso != null && (esAdmin || otroIngreso.getUsuario().getIdUsuario() == idUsuario)) {
                    req.setAttribute("otroIngresoAEditar", otroIngreso);
                }
            }
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudieron cargar los otros ingresos: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesUsuario/OtrosIngresos.jsp").forward(req, resp);
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
                agregarOtroIngreso(req, idUsuario, esAdmin(req));
            } else if ("editar".equals(action)) {
                editarOtroIngreso(req);
            } else if ("marcarCobrado".equals(action)) {
                otroIngresoServicio.cambiarEstadoOtroIngreso(Integer.parseInt(req.getParameter("idOtroIngreso")), EstadoCobro.COBRADO.name());
                req.setAttribute("exito", "El otro ingreso fue marcado como cobrado.");
            } else if ("anular".equals(action)) {
                otroIngresoServicio.cambiarEstadoOtroIngreso(Integer.parseInt(req.getParameter("idOtroIngreso")), EstadoCobro.ANULADO.name());
                req.setAttribute("exito", "El otro ingreso fue anulado.");
            }
        } catch (ServiceException | NumberFormatException | ParseException e) {
            req.setAttribute("error", "No se pudo completar la operacion: " + e.getMessage());
        }
        doGet(req, resp);
    }

    private void agregarOtroIngreso(HttpServletRequest req, int idUsuarioSesion, boolean esAdmin) throws ParseException {
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

        Date fechaOtroIngreso = formatoFecha.parse(fecha);
        boolean exito = otroIngresoServicio.crearOtroIngreso(idUsuario, fechaOtroIngreso, descripcion, Integer.parseInt(idCategoria),
                Double.parseDouble(valor), Integer.parseInt(idMetodo),
                estado == null || estado.isEmpty() ? EstadoCobro.PENDIENTE_DE_COBRO.name() : estado);

        req.setAttribute(exito ? "exito" : "error", exito ? "Otro ingreso registrado correctamente." : "No se pudo registrar el otro ingreso.");
    }

    private void editarOtroIngreso(HttpServletRequest req) {
        String idOtroIngreso = req.getParameter("idOtroIngreso");
        String idCategoria = req.getParameter("idCategoria");
        String valor = req.getParameter("valor");
        String idMetodo = req.getParameter("idMetodo");
        String descripcion = req.getParameter("descripcion");
        String estado = req.getParameter("estado");

        if (idOtroIngreso == null || idOtroIngreso.isEmpty()) {
            req.setAttribute("error", "Falta el identificador del otro ingreso.");
            return;
        }

        boolean exito = otroIngresoServicio.editarOtroIngreso(Integer.parseInt(idOtroIngreso), descripcion, Integer.parseInt(idCategoria),
                Double.parseDouble(valor), Integer.parseInt(idMetodo), estado);

        req.setAttribute(exito ? "exito" : "error", exito ? "Otro ingreso actualizado correctamente." : "No se pudo actualizar el otro ingreso.");
    }
}
