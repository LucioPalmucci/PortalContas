package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.dominio.OtroEgreso;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.OtroEgresoFactory;
import edu.usal.jdbc.implementacion.OtroEgresoDAOImplDb;
import edu.usal.jdbc.interfaz.IOtroEgresoDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class OtroEgresoServicio {

    private final IOtroEgresoDAO otroEgresoDAO;

    public OtroEgresoServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.otroEgreso");
        this.otroEgresoDAO = OtroEgresoFactory.getOtroEgresoDAO(fuente);
    }

    public boolean crearOtroEgreso(int idUsuario, Date fecha, String descripcion, int idCategoria, double valor, int idMetodo, String estadoInicial) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((OtroEgresoDAOImplDb) otroEgresoDAO).setHibernateSession(session);
            Usuario usuario = session.get(Usuario.class, idUsuario);
            CategoriaConcepto categoria = session.get(CategoriaConcepto.class, idCategoria);
            MetodoOperacion metodo = session.get(MetodoOperacion.class, idMetodo);
            OtroEgreso otroEgreso = new OtroEgreso(usuario, fecha, descripcion, EstadoPago.valueOf(estadoInicial), categoria, valor, metodo);
            session.beginTransaction();
            exito = otroEgresoDAO.guardarOtroEgreso(otroEgreso);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al crear otro egreso: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public OtroEgreso obtenerOtroEgresoPorId(int idOtroEgreso) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        OtroEgreso otroEgreso;
        try {
            ((OtroEgresoDAOImplDb) otroEgresoDAO).setHibernateSession(session);
            otroEgreso = otroEgresoDAO.obtenerOtroEgresoPorId(idOtroEgreso);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener otro egreso por id: " + e.getMessage());
        } finally {
            session.close();
        }
        return otroEgreso;
    }

    public List<OtroEgreso> obtenerOtrosEgresosPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<OtroEgreso> otrosEgresos;
        try {
            ((OtroEgresoDAOImplDb) otroEgresoDAO).setHibernateSession(session);
            otrosEgresos = otroEgresoDAO.obtenerOtrosEgresosPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener otros egresos del usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return otrosEgresos;
    }

    public List<OtroEgreso> obtenerOtrosEgresosPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<OtroEgreso> otrosEgresos;
        try {
            ((OtroEgresoDAOImplDb) otroEgresoDAO).setHibernateSession(session);
            otrosEgresos = otroEgresoDAO.obtenerOtrosEgresosPorUsuarioYPeriodo(idUsuario, desde, hasta);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener otros egresos del periodo: " + e.getMessage());
        } finally {
            session.close();
        }
        return otrosEgresos;
    }

    public List<OtroEgreso> obtenerOtrosEgresosPendientesPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<OtroEgreso> otrosEgresos;
        try {
            ((OtroEgresoDAOImplDb) otroEgresoDAO).setHibernateSession(session);
            otrosEgresos = otroEgresoDAO.obtenerOtrosEgresosPendientesPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener otros egresos pendientes: " + e.getMessage());
        } finally {
            session.close();
        }
        return otrosEgresos;
    }

    public List<OtroEgreso> obtenerTodosLosOtrosEgresos() throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<OtroEgreso> otrosEgresos;
        try {
            ((OtroEgresoDAOImplDb) otroEgresoDAO).setHibernateSession(session);
            otrosEgresos = otroEgresoDAO.obtenerTodosLosOtrosEgresos();
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener todos los otros egresos: " + e.getMessage());
        } finally {
            session.close();
        }
        return otrosEgresos;
    }

    public boolean editarOtroEgreso(int idOtroEgreso, String descripcion, int idCategoria, double valor, int idMetodo, String estado) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((OtroEgresoDAOImplDb) otroEgresoDAO).setHibernateSession(session);
            CategoriaConcepto categoria = session.get(CategoriaConcepto.class, idCategoria);
            MetodoOperacion metodo = session.get(MetodoOperacion.class, idMetodo);
            OtroEgreso otroEgreso = new OtroEgreso(idOtroEgreso, null, null, descripcion, EstadoPago.valueOf(estado), categoria, valor, metodo);
            session.beginTransaction();
            exito = otroEgresoDAO.editarOtroEgreso(otroEgreso);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al editar otro egreso: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean cambiarEstadoOtroEgreso(int idOtroEgreso, String nuevoEstado) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((OtroEgresoDAOImplDb) otroEgresoDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = otroEgresoDAO.cambiarEstadoOtroEgreso(idOtroEgreso, nuevoEstado);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al cambiar estado del otro egreso: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }
}
