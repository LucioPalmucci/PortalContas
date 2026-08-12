package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.IUsuarioDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class UsuarioDAOImplDb implements IUsuarioDAO {

    private Session hybernateSession;

    public UsuarioDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public Usuario obtenerUsuarioPorId(int idUsuario) throws HQLException {
        try {
            return hybernateSession.get(Usuario.class, idUsuario);
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener el usuario por id: " + e.getMessage());
        }
    }

    @Override
    public Usuario obtenerUsuarioPorNombreUsuario(String nombreUsuario) throws HQLException {
        try {
            Query<Usuario> query = hybernateSession.createQuery(
                    "from Usuario u where lower(u.nombreUsuario) = :nombreUsuario", Usuario.class);
            query.setParameter("nombreUsuario", nombreUsuario == null ? null : nombreUsuario.toLowerCase());
            return query.uniqueResult();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener el usuario por nombre de usuario: " + e.getMessage());
        }
    }

    @Override
    public Usuario obtenerUsuarioPorCorreo(String correo) throws HQLException {
        try {
            Query<Usuario> query = hybernateSession.createQuery(
                    "from Usuario u where u.correoElectronico = :correo", Usuario.class);
            query.setParameter("correo", correo);
            return query.uniqueResult();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener el usuario por correo: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() throws HQLException {
        try {
            Query<Usuario> query = hybernateSession.createQuery("from Usuario", Usuario.class);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener todos los usuarios: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> buscarUsuarios(String texto) throws HQLException {
        try {
            Query<Usuario> query = hybernateSession.createQuery(
                    "from Usuario u where lower(u.nombreCompleto) like :t or lower(u.correoElectronico) like :t or lower(u.nombreUsuario) like :t",
                    Usuario.class);
            query.setParameter("t", "%" + texto.toLowerCase() + "%");
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al buscar usuarios: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarUsuario(Usuario usuario) throws HQLException {
        try {
            hybernateSession.save(usuario);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean editarUsuario(Usuario usuario, int idUsuarioActor) throws HQLException {
        try {
            Usuario existente = hybernateSession.get(Usuario.class, usuario.getIdUsuario());
            if (existente == null) {
                return false;
            }
            existente.setNombreCompleto(usuario.getNombreCompleto());
            existente.setTelefono(usuario.getTelefono());
            existente.setCorreoElectronico(usuario.getCorreoElectronico());
            existente.setDescripcion(usuario.getDescripcion());
            existente.setRol(usuario.getRol());
            existente.setModificadoPor(hybernateSession.get(Usuario.class, idUsuarioActor));
            existente.setFechaModificacion(new Date());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar el usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizarContrasena(int idUsuario, String nuevaContrasenaHash) throws HQLException {
        try {
            Usuario existente = hybernateSession.get(Usuario.class, idUsuario);
            if (existente == null) {
                return false;
            }
            existente.setContrasena(nuevaContrasenaHash);
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al actualizar la contraseña del usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean activarDesactivarUsuario(int idUsuario, boolean estaActivo, int idUsuarioActor) throws HQLException {
        try {
            Usuario existente = hybernateSession.get(Usuario.class, idUsuario);
            if (existente == null) {
                return false;
            }
            existente.setEstaActivo(estaActivo);
            existente.setModificadoPor(hybernateSession.get(Usuario.class, idUsuarioActor));
            existente.setFechaModificacion(new Date());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al activar/desactivar el usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizarUltimoAcceso(int idUsuario, Date fecha) throws HQLException {
        try {
            Usuario existente = hybernateSession.get(Usuario.class, idUsuario);
            if (existente == null) {
                return false;
            }
            existente.setUltimoAcceso(fecha);
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al actualizar el ultimo acceso del usuario: " + e.getMessage());
        }
    }
}
