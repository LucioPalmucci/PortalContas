package edu.usal.servlet;

import edu.usal.jdbc.dominio.Rol;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.UsuarioServicio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/Usuario")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioServicio usuarioServicio;

    public UsuarioServlet() {
        this.usuarioServicio = new UsuarioServicio();
    }

    private boolean esAdmin(HttpServletRequest req) {
        return "ADMINISTRADOR".equals(req.getSession().getAttribute("rolUsuario"));
    }

    // Administrador que ejecuta la operacion de ABM, para el registro de auditoria (Usuario.modificadoPor/fechaModificacion).
    private int idUsuarioActor(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("idUsuario");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
            return;
        }
        try {
            req.setAttribute("usuarios", usuarioServicio.obtenerTodosLosUsuarios());

            String editarId = req.getParameter("editarId");
            if (editarId != null && !editarId.isEmpty()) {
                req.setAttribute("usuarioAEditar", usuarioServicio.obtenerUsuarioPorId(Integer.parseInt(editarId)));
            }
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesAdministrador/Usuarios.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
            return;
        }
        String action = req.getParameter("action");
        try {
            if ("agregar".equals(action)) {
                agregarUsuario(req);
            } else if ("editar".equals(action)) {
                editarUsuario(req);
            } else if ("cambiarContrasena".equals(action)) {
                cambiarContrasena(req);
            } else if ("activar".equals(action)) {
                usuarioServicio.activarDesactivarUsuario(Integer.parseInt(req.getParameter("idUsuario")), true, idUsuarioActor(req));
                req.setAttribute("exito", "Usuario activado.");
            } else if ("desactivar".equals(action)) {
                usuarioServicio.activarDesactivarUsuario(Integer.parseInt(req.getParameter("idUsuario")), false, idUsuarioActor(req));
                req.setAttribute("exito", "Usuario desactivado.");
            }
        } catch (ServiceException | IllegalArgumentException e) {
            req.setAttribute("error", "No se pudo completar la operacion: " + e.getMessage());
        }
        doGet(req, resp);
    }

    private void agregarUsuario(HttpServletRequest req) {
        String nombreCompleto = req.getParameter("nombreCompleto");
        String telefono = req.getParameter("telefono");
        String correo = req.getParameter("correoElectronico");
        String contrasena = req.getParameter("contrasena");
        String descripcion = req.getParameter("descripcion");
        String rol = req.getParameter("rol");
        String nombreUsuario = req.getParameter("nombreUsuario");

        if (nombreCompleto == null || nombreCompleto.isEmpty() || correo == null || correo.isEmpty()
                || contrasena == null || contrasena.isEmpty() || nombreUsuario == null || nombreUsuario.isEmpty() || rol == null) {
            req.setAttribute("error", "Complete todos los campos obligatorios.");
            return;
        }
        if (!esContraseniaValida(contrasena)) {
            req.setAttribute("error", "La contraseña debe tener al menos 8 caracteres, una mayuscula y un numero.");
            return;
        }

        boolean exito = usuarioServicio.crearUsuario(nombreCompleto, telefono, correo, contrasena, descripcion, Rol.valueOf(rol), nombreUsuario, idUsuarioActor(req));
        req.setAttribute(exito ? "exito" : "error", exito ? "Usuario creado correctamente." : "No se pudo crear el usuario.");
    }

    private void editarUsuario(HttpServletRequest req) {
        String idUsuario = req.getParameter("idUsuario");
        String nombreCompleto = req.getParameter("nombreCompleto");
        String telefono = req.getParameter("telefono");
        String correo = req.getParameter("correoElectronico");
        String descripcion = req.getParameter("descripcion");
        String rol = req.getParameter("rol");

        if (idUsuario == null || idUsuario.isEmpty()) {
            req.setAttribute("error", "Falta el identificador del usuario.");
            return;
        }

        boolean exito = usuarioServicio.editarUsuario(Integer.parseInt(idUsuario), nombreCompleto, telefono, correo, descripcion, Rol.valueOf(rol), idUsuarioActor(req));
        req.setAttribute(exito ? "exito" : "error", exito ? "Usuario actualizado correctamente." : "No se pudo actualizar el usuario.");
    }

    private void cambiarContrasena(HttpServletRequest req) {
        String idUsuario = req.getParameter("idUsuario");
        String nuevaContrasena = req.getParameter("nuevaContrasena");

        if (idUsuario == null || idUsuario.isEmpty() || !esContraseniaValida(nuevaContrasena)) {
            req.setAttribute("error", "La nueva contraseña debe tener al menos 8 caracteres, una mayuscula y un numero.");
            return;
        }

        boolean exito = usuarioServicio.cambiarContrasena(Integer.parseInt(idUsuario), nuevaContrasena);
        req.setAttribute(exito ? "exito" : "error", exito ? "Contraseña actualizada." : "No se pudo actualizar la contraseña.");
    }

    private boolean esContraseniaValida(String contrasena) {
        return contrasena != null
                && contrasena.length() >= 8
                && contrasena.chars().anyMatch(Character::isUpperCase)
                && contrasena.chars().anyMatch(Character::isDigit);
    }
}
