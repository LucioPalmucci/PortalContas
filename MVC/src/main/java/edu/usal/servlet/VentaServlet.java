package edu.usal.servlet;

import edu.usal.jdbc.dominio.EstadoCobro;
import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.dominio.Venta;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.MetodoOperacionServicio;
import edu.usal.jdbc.servicio.UsuarioServicio;
import edu.usal.jdbc.servicio.VentaServicio;

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

@WebServlet("/Venta")
public class VentaServlet extends HttpServlet {

    private final VentaServicio ventaServicio;
    private final MetodoOperacionServicio metodoOperacionServicio;
    private final UsuarioServicio usuarioServicio;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    public VentaServlet() {
        this.ventaServicio = new VentaServicio();
        this.metodoOperacionServicio = new MetodoOperacionServicio();
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
            List<Venta> ventas;
            if (esAdmin) {
                req.setAttribute("clientes", usuarioServicio.obtenerTodosLosUsuarios());
                String idUsuarioFiltro = req.getParameter("idUsuarioFiltro");
                ventas = (idUsuarioFiltro != null && !idUsuarioFiltro.isEmpty())
                        ? ventaServicio.obtenerVentasPorUsuario(Integer.parseInt(idUsuarioFiltro))
                        : ventaServicio.obtenerTodasLasVentas();
                req.setAttribute("idUsuarioFiltro", idUsuarioFiltro);
            } else {
                ventas = ventaServicio.obtenerVentasPorUsuario(idUsuario);
            }
            req.setAttribute("ventas", ventas);
            req.setAttribute("esAdmin", esAdmin);
            req.setAttribute("metodosCobro", metodoOperacionServicio.obtenerMetodosPorTipo("COBRO"));

            List<Venta> ventasActivas = new java.util.ArrayList<>();
            for (Venta v : ventas) {
                if (v.getEstado() != EstadoCobro.ANULADO) ventasActivas.add(v);
            }
            req.setAttribute("evolucionMensual", EvolucionMensualUtil.calcularUltimos12Meses(ventasActivas, Venta::getFecha, Venta::getSubtotal));

            String editarId = req.getParameter("editarId");
            if (editarId != null && !editarId.isEmpty()) {
                Venta venta = ventaServicio.obtenerVentaPorId(Integer.parseInt(editarId));
                if (venta != null && (esAdmin || venta.getUsuario().getIdUsuario() == idUsuario)) {
                    req.setAttribute("ventaAEditar", venta);
                }
            }
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudieron cargar las ventas: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesUsuario/Ventas.jsp").forward(req, resp);
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
                agregarVenta(req, idUsuario, esAdmin(req));
            } else if ("editar".equals(action)) {
                editarVenta(req);
            } else if ("marcarCobrada".equals(action)) {
                ventaServicio.cambiarEstadoVenta(Integer.parseInt(req.getParameter("idVenta")), EstadoCobro.COBRADO.name());
                req.setAttribute("exito", "La venta fue marcada como cobrada.");
            } else if ("anular".equals(action)) {
                ventaServicio.cambiarEstadoVenta(Integer.parseInt(req.getParameter("idVenta")), EstadoCobro.ANULADO.name());
                req.setAttribute("exito", "La venta fue anulada.");
            }
        } catch (ServiceException | NumberFormatException | ParseException e) {
            req.setAttribute("error", "No se pudo completar la operacion: " + e.getMessage());
        }
        doGet(req, resp);
    }

    private void agregarVenta(HttpServletRequest req, int idUsuarioSesion, boolean esAdmin) throws ParseException {
        String fecha = req.getParameter("fecha");
        String concepto = req.getParameter("conceptoVenta");
        String precioUnitario = req.getParameter("precioUnitario");
        String cantidad = req.getParameter("cantidad");
        String idMetodo = req.getParameter("idMetodo");
        String descripcion = req.getParameter("descripcion");
        String estado = req.getParameter("estado");
        String idUsuarioDestino = req.getParameter("idUsuarioDestino");

        if (fecha == null || fecha.isEmpty() || concepto == null || concepto.isEmpty()
                || precioUnitario == null || precioUnitario.isEmpty() || cantidad == null || cantidad.isEmpty()
                || idMetodo == null || idMetodo.isEmpty()
                || (esAdmin && (idUsuarioDestino == null || idUsuarioDestino.isEmpty()))) {
            req.setAttribute("error", "Complete todos los campos obligatorios.");
            return;
        }

        int idUsuario = (esAdmin && idUsuarioDestino != null && !idUsuarioDestino.isEmpty())
                ? Integer.parseInt(idUsuarioDestino) : idUsuarioSesion;

        Date fechaVenta = formatoFecha.parse(fecha);
        boolean exito = ventaServicio.crearVenta(idUsuario, fechaVenta, descripcion, concepto,
                Double.parseDouble(precioUnitario), Integer.parseInt(cantidad), Integer.parseInt(idMetodo),
                estado == null || estado.isEmpty() ? EstadoCobro.PENDIENTE_DE_COBRO.name() : estado);

        req.setAttribute(exito ? "exito" : "error", exito ? "Venta registrada correctamente." : "No se pudo registrar la venta.");
    }

    private void editarVenta(HttpServletRequest req) throws ParseException {
        String idVenta = req.getParameter("idVenta");
        String fecha = req.getParameter("fecha");
        String concepto = req.getParameter("conceptoVenta");
        String precioUnitario = req.getParameter("precioUnitario");
        String cantidad = req.getParameter("cantidad");
        String idMetodo = req.getParameter("idMetodo");
        String descripcion = req.getParameter("descripcion");
        String estado = req.getParameter("estado");

        if (idVenta == null || idVenta.isEmpty()) {
            req.setAttribute("error", "Falta el identificador de la venta.");
            return;
        }

        Venta venta = new Venta();
        venta.setIdVenta(Integer.parseInt(idVenta));
        venta.setFecha(formatoFecha.parse(fecha));
        venta.setConceptoVenta(concepto);
        venta.setDescripcion(descripcion);
        venta.setPrecioUnitario(Double.parseDouble(precioUnitario));
        venta.setCantidad(Integer.parseInt(cantidad));
        venta.setEstado(EstadoCobro.valueOf(estado));
        MetodoOperacion metodo = new MetodoOperacion();
        metodo.setIdMetodo(Integer.parseInt(idMetodo));
        venta.setMetodo(metodo);

        boolean exito = ventaServicio.editarVenta(venta);
        req.setAttribute(exito ? "exito" : "error", exito ? "Venta actualizada correctamente." : "No se pudo actualizar la venta.");
    }
}
