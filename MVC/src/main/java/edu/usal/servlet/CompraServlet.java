package edu.usal.servlet;

import edu.usal.jdbc.dominio.Compra;
import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.CompraServicio;
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

@WebServlet("/Compra")
public class CompraServlet extends HttpServlet {

    private final CompraServicio compraServicio;
    private final MetodoOperacionServicio metodoOperacionServicio;
    private final UsuarioServicio usuarioServicio;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    public CompraServlet() {
        this.compraServicio = new CompraServicio();
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
            List<Compra> compras;
            if (esAdmin) {
                req.setAttribute("clientes", usuarioServicio.obtenerTodosLosUsuarios());
                String idUsuarioFiltro = req.getParameter("idUsuarioFiltro");
                compras = (idUsuarioFiltro != null && !idUsuarioFiltro.isEmpty())
                        ? compraServicio.obtenerComprasPorUsuario(Integer.parseInt(idUsuarioFiltro))
                        : compraServicio.obtenerTodasLasCompras();
                req.setAttribute("idUsuarioFiltro", idUsuarioFiltro);
            } else {
                compras = compraServicio.obtenerComprasPorUsuario(idUsuario);
            }
            req.setAttribute("compras", compras);
            req.setAttribute("esAdmin", esAdmin);
            req.setAttribute("metodosPago", metodoOperacionServicio.obtenerMetodosPorTipo("PAGO"));

            List<Compra> comprasActivas = new java.util.ArrayList<>();
            for (Compra c : compras) {
                if (c.getEstado() != EstadoPago.ANULADO) comprasActivas.add(c);
            }
            req.setAttribute("evolucionMensual", EvolucionMensualUtil.calcularUltimos12Meses(comprasActivas, Compra::getFecha, Compra::getValorTotal));

            String editarId = req.getParameter("editarId");
            if (editarId != null && !editarId.isEmpty()) {
                Compra compra = compraServicio.obtenerCompraPorId(Integer.parseInt(editarId));
                if (compra != null && (esAdmin || compra.getUsuario().getIdUsuario() == idUsuario)) {
                    req.setAttribute("compraAEditar", compra);
                }
            }
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudieron cargar las compras: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesUsuario/Compras.jsp").forward(req, resp);
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
                agregarCompra(req, idUsuario, esAdmin(req));
            } else if ("editar".equals(action)) {
                editarCompra(req);
            } else if ("marcarPagada".equals(action)) {
                compraServicio.cambiarEstadoCompra(Integer.parseInt(req.getParameter("idCompra")), EstadoPago.PAGADO.name());
                req.setAttribute("exito", "La compra fue marcada como pagada.");
            } else if ("anular".equals(action)) {
                compraServicio.cambiarEstadoCompra(Integer.parseInt(req.getParameter("idCompra")), EstadoPago.ANULADO.name());
                req.setAttribute("exito", "La compra fue anulada.");
            }
        } catch (ServiceException | NumberFormatException | ParseException e) {
            req.setAttribute("error", "No se pudo completar la operacion: " + e.getMessage());
        }
        doGet(req, resp);
    }

    private void agregarCompra(HttpServletRequest req, int idUsuarioSesion, boolean esAdmin) throws ParseException {
        String fecha = req.getParameter("fecha");
        String concepto = req.getParameter("conceptoCompra");
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

        Date fechaCompra = formatoFecha.parse(fecha);
        boolean exito = compraServicio.crearCompra(idUsuario, fechaCompra, descripcion, concepto,
                Double.parseDouble(precioUnitario), Integer.parseInt(cantidad), Integer.parseInt(idMetodo),
                estado == null || estado.isEmpty() ? EstadoPago.PENDIENTE_DE_PAGO.name() : estado);

        req.setAttribute(exito ? "exito" : "error", exito ? "Compra registrada correctamente." : "No se pudo registrar la compra.");
    }

    private void editarCompra(HttpServletRequest req) throws ParseException {
        String idCompra = req.getParameter("idCompra");
        String fecha = req.getParameter("fecha");
        String concepto = req.getParameter("conceptoCompra");
        String precioUnitario = req.getParameter("precioUnitario");
        String cantidad = req.getParameter("cantidad");
        String idMetodo = req.getParameter("idMetodo");
        String descripcion = req.getParameter("descripcion");
        String estado = req.getParameter("estado");

        if (idCompra == null || idCompra.isEmpty()) {
            req.setAttribute("error", "Falta el identificador de la compra.");
            return;
        }

        Compra compra = new Compra();
        compra.setIdCompra(Integer.parseInt(idCompra));
        compra.setFecha(formatoFecha.parse(fecha));
        compra.setConceptoCompra(concepto);
        compra.setDescripcion(descripcion);
        compra.setPrecioUnitario(Double.parseDouble(precioUnitario));
        compra.setCantidad(Integer.parseInt(cantidad));
        compra.setEstado(EstadoPago.valueOf(estado));
        MetodoOperacion metodo = new MetodoOperacion();
        metodo.setIdMetodo(Integer.parseInt(idMetodo));
        compra.setMetodo(metodo);

        boolean exito = compraServicio.editarCompra(compra);
        req.setAttribute(exito ? "exito" : "error", exito ? "Compra actualizada correctamente." : "No se pudo actualizar la compra.");
    }
}
