package edu.usal.servlet;

import edu.usal.jdbc.dominio.Rol;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.UsuarioServicio;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class loginServlet extends HttpServlet {

    private static final int SESION_NORMAL_SEGUNDOS = 30 * 60;
    private static final int SESION_RECORDARME_SEGUNDOS = 7 * 24 * 60 * 60;

    private final UsuarioServicio usuarioServicio;

    public loginServlet() {
        this.usuarioServicio = new UsuarioServicio();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("logout".equals(action)) {
            cerrarSesion(req, resp);
            return;
        }

        var session = req.getSession();
        if (session.getAttribute("idUsuario") != null) {
            redirigirSegunRol(req, resp, (String) session.getAttribute("rolUsuario"));
            return;
        }

        if ("login".equals(action)) {
            String nombreUsuario = req.getParameter("usuario");
            String contrasenia = req.getParameter("contrasenia");
            boolean recordarme = "on".equals(req.getParameter("recordarme"));

            if (nombreUsuario == null || contrasenia == null || nombreUsuario.trim().isEmpty() || contrasenia.trim().isEmpty()) {
                mostrarError(req, resp, "Por favor complete usuario y contraseña.");
                return;
            }

            Usuario usuario;
            try {
                usuario = usuarioServicio.verificarCredenciales(nombreUsuario.trim(), contrasenia);
            } catch (ServiceException e) {
                mostrarError(req, resp, "Ocurrio un problema al iniciar sesion. Intente nuevamente.");
                return;
            }

            if (usuario == null) {
                mostrarError(req, resp, "Usuario o contraseña incorrectos.");
                return;
            }

            crearSesion(req, resp, usuario, recordarme);
            return;
        }

        req.getRequestDispatcher("/Vistas/Principal.jsp").forward(req, resp);
    }

    private void mostrarError(HttpServletRequest req, HttpServletResponse resp, String mensaje) throws ServletException, IOException {
        req.setAttribute("error", mensaje);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/Vistas/Principal.jsp");
        dispatcher.forward(req, resp);
    }

    private void crearSesion(HttpServletRequest req, HttpServletResponse resp, Usuario usuario, boolean recordarme) throws IOException {
        var session = req.getSession(true);
        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("rolUsuario", usuario.getRol().name());
        session.setAttribute("nombreUsuarioSesion", usuario.getNombreCompleto());

        int duracion = recordarme ? SESION_RECORDARME_SEGUNDOS : SESION_NORMAL_SEGUNDOS;
        session.setMaxInactiveInterval(duracion);

        Cookie cookie = new Cookie("nombreUsuario", usuario.getNombreCompleto().replace(" ", "_"));
        cookie.setMaxAge(recordarme ? SESION_RECORDARME_SEGUNDOS : -1);
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        resp.addCookie(cookie);

        redirigirSegunRol(req, resp, usuario.getRol().name());
    }

    private void redirigirSegunRol(HttpServletRequest req, HttpServletResponse resp, String rol) throws IOException {
        String destino = Rol.ADMINISTRADOR.name().equals(rol)
                ? "/Vistas/OpcionesAdmin.jsp"
                : "/Vistas/OpcionesUsuario/Panel.jsp";
        resp.sendRedirect(req.getContextPath() + destino);
    }

    private void cerrarSesion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("nombreUsuario".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                    resp.addCookie(cookie);
                }
            }
        }
        resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
