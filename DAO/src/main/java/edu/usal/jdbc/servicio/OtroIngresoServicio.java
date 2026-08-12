package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.dominio.EstadoCobro;
import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.dominio.OtroIngreso;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.OtroIngresoFactory;
import edu.usal.jdbc.implementacion.OtroIngresoDAOImplDb;
import edu.usal.jdbc.interfaz.IOtroIngresoDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class OtroIngresoServicio {

    private final IOtroIngresoDAO otroIngresoDAO;

    public OtroIngresoServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.otroIngreso");
        this.otroIngresoDAO = OtroIngresoFactory.getOtroIngresoDAO(fuente);
    }

    public boolean crearOtroIngreso(int idUsuario, Date fecha, String descripcion, int idCategoria, double valor, int idMetodo, String estadoInicial) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((OtroIngresoDAOImplDb) otroIngresoDAO).setHibernateSession(session);
            Usuario usuario = session.get(Usuario.class, idUsuario);
            CategoriaConcepto categoria = session.get(CategoriaConcepto.class, idCategoria);
            MetodoOperacion metodo = session.get(MetodoOperacion.class, idMetodo);
            OtroIngreso otroIngreso = new OtroIngreso(usuario, fecha, descripcion, EstadoCobro.valueOf(estadoInicial), categoria, valor, metodo);
            session.beginTransaction();
            exito = otroIngresoDAO.guardarOtroIngreso(otroIngreso);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al crear otro ingreso: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public OtroIngreso obtenerOtroIngresoPorId(int idOtroIngreso) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        OtroIngreso otroIngreso;
        try {
            ((OtroIngresoDAOImplDb) otroIngresoDAO).setHibernateSession(session);
            otroIngreso = otroIngresoDAO.obtenerOtroIngresoPorId(idOtroIngreso);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener otro ingreso por id: " + e.getMessage());
        } finally {
            session.close();
        }
        return otroIngreso;
    }

    public List<OtroIngreso> obtenerOtrosIngresosPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<OtroIngreso> otrosIngresos;
        try {
            ((OtroIngresoDAOImplDb) otroIngresoDAO).setHibernateSession(session);
            otrosIngresos = otroIngresoDAO.obtenerOtrosIngresosPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener otros ingresos del usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return otrosIngresos;
    }

    public List<OtroIngreso> obtenerOtrosIngresosPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<OtroIngreso> otrosIngresos;
        try {
            ((OtroIngresoDAOImplDb) otroIngresoDAO).setHibernateSession(session);
            otrosIngresos = otroIngresoDAO.obtenerOtrosIngresosPorUsuarioYPeriodo(idUsuario, desde, hasta);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener otros ingresos del periodo: " + e.getMessage());
        } finally {
            session.close();
        }
        return otrosIngresos;
    }

    public List<OtroIngreso> obtenerOtrosIngresosPendientesPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<OtroIngreso> otrosIngresos;
        try {
            ((OtroIngresoDAOImplDb) otroIngresoDAO).setHibernateSession(session);
            otrosIngresos = otroIngresoDAO.obtenerOtrosIngresosPendientesPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener otros ingresos pendientes: " + e.getMessage());
        } finally {
            session.close();
        }
        return otrosIngresos;
    }

    public List<OtroIngreso> obtenerTodosLosOtrosIngresos() throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<OtroIngreso> otrosIngresos;
        try {
            ((OtroIngresoDAOImplDb) otroIngresoDAO).setHibernateSession(session);
            otrosIngresos = otroIngresoDAO.obtenerTodosLosOtrosIngresos();
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener todos los otros ingresos: " + e.getMessage());
        } finally {
            session.close();
        }
        return otrosIngresos;
    }

    public boolean editarOtroIngreso(int idOtroIngreso, String descripcion, int idCategoria, double valor, int idMetodo, String estado) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((OtroIngresoDAOImplDb) otroIngresoDAO).setHibernateSession(session);
            CategoriaConcepto categoria = session.get(CategoriaConcepto.class, idCategoria);
            MetodoOperacion metodo = session.get(MetodoOperacion.class, idMetodo);
            OtroIngreso otroIngreso = new OtroIngreso(idOtroIngreso, null, null, descripcion, EstadoCobro.valueOf(estado), categoria, valor, metodo);
            session.beginTransaction();
            exito = otroIngresoDAO.editarOtroIngreso(otroIngreso);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al editar otro ingreso: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean cambiarEstadoOtroIngreso(int idOtroIngreso, String nuevoEstado) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((OtroIngresoDAOImplDb) otroIngresoDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = otroIngresoDAO.cambiarEstadoOtroIngreso(idOtroIngreso, nuevoEstado);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al cambiar estado del otro ingreso: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }
}
