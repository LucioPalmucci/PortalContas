package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.MetodoOperacionFactory;
import edu.usal.jdbc.implementacion.MetodoOperacionDAOImplDb;
import edu.usal.jdbc.interfaz.IMetodoOperacionDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class MetodoOperacionServicio {

    private final IMetodoOperacionDAO metodoOperacionDAO;

    public MetodoOperacionServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.metodoOperacion");
        this.metodoOperacionDAO = MetodoOperacionFactory.getMetodoOperacionDAO(fuente);
    }

    public List<MetodoOperacion> obtenerTodosLosMetodos() throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<MetodoOperacion> metodos;
        try {
            ((MetodoOperacionDAOImplDb) metodoOperacionDAO).setHibernateSession(session);
            metodos = metodoOperacionDAO.obtenerTodosLosMetodos();
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener todos los metodos: " + e.getMessage());
        } finally {
            session.close();
        }
        return metodos;
    }

    public List<MetodoOperacion> obtenerMetodosPorTipo(String cobroOPago) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<MetodoOperacion> metodos;
        try {
            ((MetodoOperacionDAOImplDb) metodoOperacionDAO).setHibernateSession(session);
            metodos = metodoOperacionDAO.obtenerMetodosPorTipo(cobroOPago);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener metodos por tipo: " + e.getMessage());
        } finally {
            session.close();
        }
        return metodos;
    }

    public boolean guardarMetodo(String nombre, String cobroOPago) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((MetodoOperacionDAOImplDb) metodoOperacionDAO).setHibernateSession(session);
            MetodoOperacion metodo = new MetodoOperacion(nombre, edu.usal.jdbc.dominio.CobroOPago.valueOf(cobroOPago));
            session.beginTransaction();
            exito = metodoOperacionDAO.guardarMetodo(metodo);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al guardar metodo: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean cambiarEstadoMetodo(int id, boolean estaActivo) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((MetodoOperacionDAOImplDb) metodoOperacionDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = metodoOperacionDAO.cambiarEstadoMetodo(id, estaActivo);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al cambiar el estado del metodo: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }
}
