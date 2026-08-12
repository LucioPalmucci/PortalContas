package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.Rol;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.UsuarioFactory;
import edu.usal.jdbc.implementacion.UsuarioDAOImplDb;
import edu.usal.jdbc.interfaz.IUsuarioDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import edu.usal.jdbc.util.PasswordUtil;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class UsuarioServicio {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.usuario");
        this.usuarioDAO = UsuarioFactory.getUsuarioDAO(fuente);
    }

    public boolean crearUsuario(String nombreCompleto, String telefono, String correoElectronico, String contrasenaPlana, String descripcion, Rol rol, String nombreUsuario, int idUsuarioActor) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((UsuarioDAOImplDb) usuarioDAO).setHibernateSession(session);
            if (usuarioDAO.obtenerUsuarioPorNombreUsuario(nombreUsuario) != null) {
                throw new ServiceException("Ya existe un usuario con ese nombre de usuario.");
            }
            if (usuarioDAO.obtenerUsuarioPorCorreo(correoElectronico) != null) {
                throw new ServiceException("Ya existe un usuario con ese correo electronico.");
            }
            Usuario usuarioACrear = new Usuario(nombreCompleto, telefono, correoElectronico, PasswordUtil.hash(contrasenaPlana), true, descripcion, rol, nombreUsuario, new Date(), null);
            usuarioACrear.setModificadoPor(session.get(Usuario.class, idUsuarioActor));
            usuarioACrear.setFechaModificacion(new Date());
            session.beginTransaction();
            exito = usuarioDAO.guardarUsuario(usuarioACrear);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al crear usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public Usuario verificarCredenciales(String nombreUsuario, String contrasenaPlana) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            ((UsuarioDAOImplDb) usuarioDAO).setHibernateSession(session);
            Usuario usuario = usuarioDAO.obtenerUsuarioPorNombreUsuario(nombreUsuario);
            if (usuario == null || !usuario.isEstaActivo()) {
                return null;
            }
            if (!PasswordUtil.hash(contrasenaPlana).equals(usuario.getContrasena())) {
                return null;
            }
            session.beginTransaction();
            usuarioDAO.actualizarUltimoAcceso(usuario.getIdUsuario(), new Date());
            session.getTransaction().commit();
            return usuario;
        } catch (HQLException e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) session.getTransaction().rollback();
            throw new ServiceException("Error al verificar credenciales: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    public Usuario obtenerUsuarioPorId(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuario usuario;
        try {
            ((UsuarioDAOImplDb) usuarioDAO).setHibernateSession(session);
            usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener usuario por id: " + e.getMessage());
        } finally {
            session.close();
        }
        return usuario;
    }

    public List<Usuario> obtenerTodosLosUsuarios() throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Usuario> usuarios;
        try {
            ((UsuarioDAOImplDb) usuarioDAO).setHibernateSession(session);
            usuarios = usuarioDAO.obtenerTodosLosUsuarios();
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener todos los usuarios: " + e.getMessage());
        } finally {
            session.close();
        }
        return usuarios;
    }

    public List<Usuario> buscarUsuarios(String texto) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Usuario> usuarios;
        try {
            ((UsuarioDAOImplDb) usuarioDAO).setHibernateSession(session);
            usuarios = usuarioDAO.buscarUsuarios(texto);
        } catch (HQLException e) {
            throw new ServiceException("Error al buscar usuarios: " + e.getMessage());
        } finally {
            session.close();
        }
        return usuarios;
    }

    public boolean editarUsuario(int idUsuario, String nombreCompleto, String telefono, String correoElectronico, String descripcion, Rol rol, int idUsuarioActor) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((UsuarioDAOImplDb) usuarioDAO).setHibernateSession(session);
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            usuario.setNombreCompleto(nombreCompleto);
            usuario.setTelefono(telefono);
            usuario.setCorreoElectronico(correoElectronico);
            usuario.setDescripcion(descripcion);
            usuario.setRol(rol);
            session.beginTransaction();
            exito = usuarioDAO.editarUsuario(usuario, idUsuarioActor);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al editar usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean cambiarContrasena(int idUsuario, String nuevaContrasenaPlana) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((UsuarioDAOImplDb) usuarioDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = usuarioDAO.actualizarContrasena(idUsuario, PasswordUtil.hash(nuevaContrasenaPlana));
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al cambiar la contraseña: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean activarDesactivarUsuario(int idUsuario, boolean estaActivo, int idUsuarioActor) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((UsuarioDAOImplDb) usuarioDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = usuarioDAO.activarDesactivarUsuario(idUsuario, estaActivo, idUsuarioActor);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al activar/desactivar usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }
}
