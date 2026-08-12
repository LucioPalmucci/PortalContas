package edu.usal.servlet;

import edu.usal.jdbc.dominio.Compra;
import edu.usal.jdbc.dominio.Gasto;
import edu.usal.jdbc.dominio.OtroEgreso;
import edu.usal.jdbc.dominio.OtroIngreso;
import edu.usal.jdbc.dominio.Rol;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.dominio.Venta;
import edu.usal.jdbc.dto.TesoreriaDTO;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.ReporteServicio;
import edu.usal.jdbc.servicio.UsuarioServicio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Tesoreria")
public class TesoreriaServlet extends HttpServlet {

    private final ReporteServicio reporteServicio;
    private final UsuarioServicio usuarioServicio;

    public TesoreriaServlet() {
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
        try {
            TesoreriaDTO tesoreria;
            if (esAdmin) {
                req.setAttribute("clientes", usuarioServicio.obtenerTodosLosUsuarios());
                String idUsuarioFiltro = req.getParameter("idUsuarioFiltro");
                tesoreria = (idUsuarioFiltro != null && !idUsuarioFiltro.isEmpty())
                        ? reporteServicio.obtenerTesoreria(Integer.parseInt(idUsuarioFiltro))
                        : obtenerTesoreriaConsolidada();
                req.setAttribute("idUsuarioFiltro", idUsuarioFiltro);
            } else {
                tesoreria = reporteServicio.obtenerTesoreria((Integer) idUsuarioAttr);
            }
            req.setAttribute("tesoreria", tesoreria);
            req.setAttribute("esAdmin", esAdmin);
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudo calcular la tesoreria: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesUsuario/Tesoreria.jsp").forward(req, resp);
    }

    // Consolida la tesoreria de todos los clientes (rol USUARIO) sumando totales y combinando los listados pendientes.
    private TesoreriaDTO obtenerTesoreriaConsolidada() throws ServiceException {
        double cuentasPorCobrar = 0;
        double cuentasPorPagar = 0;
        List<Venta> ventasPendientes = new ArrayList<>();
        List<Compra> comprasPendientes = new ArrayList<>();
        List<Gasto> gastosPendientes = new ArrayList<>();
        List<OtroIngreso> otrosIngresosPendientes = new ArrayList<>();
        List<OtroEgreso> otrosEgresosPendientes = new ArrayList<>();

        for (Usuario cliente : usuarioServicio.obtenerTodosLosUsuarios()) {
            if (cliente.getRol() != Rol.USUARIO) continue;
            TesoreriaDTO tesoreriaCliente = reporteServicio.obtenerTesoreria(cliente.getIdUsuario());
            cuentasPorCobrar += tesoreriaCliente.getCuentasPorCobrar();
            cuentasPorPagar += tesoreriaCliente.getCuentasPorPagar();
            ventasPendientes.addAll(tesoreriaCliente.getVentasPendientes());
            comprasPendientes.addAll(tesoreriaCliente.getComprasPendientes());
            gastosPendientes.addAll(tesoreriaCliente.getGastosPendientes());
            otrosIngresosPendientes.addAll(tesoreriaCliente.getOtrosIngresosPendientes());
            otrosEgresosPendientes.addAll(tesoreriaCliente.getOtrosEgresosPendientes());
        }

        return new TesoreriaDTO(cuentasPorCobrar, cuentasPorPagar, cuentasPorCobrar - cuentasPorPagar,
                ventasPendientes, comprasPendientes, gastosPendientes, otrosIngresosPendientes, otrosEgresosPendientes);
    }
}
