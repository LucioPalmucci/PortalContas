package edu.usal.servlet;

import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.CategoriaConceptoServicio;
import edu.usal.jdbc.servicio.MetodoOperacionServicio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Catalogo")
public class CatalogoServlet extends HttpServlet {

    private final MetodoOperacionServicio metodoOperacionServicio;
    private final CategoriaConceptoServicio categoriaConceptoServicio;

    public CatalogoServlet() {
        this.metodoOperacionServicio = new MetodoOperacionServicio();
        this.categoriaConceptoServicio = new CategoriaConceptoServicio();
    }

    private boolean esAdmin(HttpServletRequest req) {
        return "ADMINISTRADOR".equals(req.getSession().getAttribute("rolUsuario"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
            return;
        }
        try {
            req.setAttribute("metodos", metodoOperacionServicio.obtenerTodosLosMetodos());
            req.setAttribute("categorias", categoriaConceptoServicio.obtenerTodasLasCategorias());
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudo cargar el catalogo: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesAdministrador/Catalogo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
            return;
        }
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "agregarMetodo":
                    metodoOperacionServicio.guardarMetodo(req.getParameter("nombreMetodo"), req.getParameter("cobroOPago"));
                    req.setAttribute("exito", "Medio de pago/cobro agregado.");
                    break;
                case "eliminarMetodo":
                    metodoOperacionServicio.eliminarMetodo(Integer.parseInt(req.getParameter("idMetodo")));
                    req.setAttribute("exito", "Medio eliminado.");
                    break;
                case "agregarCategoria":
                    categoriaConceptoServicio.guardarCategoria(req.getParameter("nombreCategoria"), req.getParameter("aplicaA"));
                    req.setAttribute("exito", "Categoria agregada.");
                    break;
                case "eliminarCategoria":
                    categoriaConceptoServicio.eliminarCategoria(Integer.parseInt(req.getParameter("idCategoria")));
                    req.setAttribute("exito", "Categoria eliminada.");
                    break;
                default:
                    break;
            }
        } catch (ServiceException | IllegalArgumentException e) {
            req.setAttribute("error", "No se pudo completar la operacion: " + e.getMessage()
                    + " Verifique que no este en uso por ninguna operacion registrada.");
        }
        doGet(req, resp);
    }
}
