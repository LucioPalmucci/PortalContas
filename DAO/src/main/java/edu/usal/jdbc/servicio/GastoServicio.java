package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.dominio.Gasto;
import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.GastoFactory;
import edu.usal.jdbc.implementacion.GastoDAOImplDb;
import edu.usal.jdbc.interfaz.IGastoDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class GastoServicio {

    private final IGastoDAO gastoDAO;

    public GastoServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.gasto");
        this.gastoDAO = GastoFactory.getGastoDAO(fuente);
    }

    public boolean crearGasto(int idUsuario, Date fecha, String descripcion, int idCategoria, double valor, int idMetodo, String estadoInicial) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((GastoDAOImplDb) gastoDAO).setHibernateSession(session);
            Usuario usuario = session.get(Usuario.class, idUsuario);
            CategoriaConcepto categoria = session.get(CategoriaConcepto.class, idCategoria);
            MetodoOperacion metodo = session.get(MetodoOperacion.class, idMetodo);
            Gasto gasto = new Gasto(usuario, fecha, descripcion, EstadoPago.valueOf(estadoInicial), categoria, valor, metodo);
            session.beginTransaction();
            exito = gastoDAO.guardarGasto(gasto);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al crear gasto: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public Gasto obtenerGastoPorId(int idGasto) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Gasto gasto;
        try {
            ((GastoDAOImplDb) gastoDAO).setHibernateSession(session);
            gasto = gastoDAO.obtenerGastoPorId(idGasto);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener gasto por id: " + e.getMessage());
        } finally {
            session.close();
        }
        return gasto;
    }

    public List<Gasto> obtenerGastosPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Gasto> gastos;
        try {
            ((GastoDAOImplDb) gastoDAO).setHibernateSession(session);
            gastos = gastoDAO.obtenerGastosPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener gastos del usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return gastos;
    }

    public List<Gasto> obtenerGastosPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Gasto> gastos;
        try {
            ((GastoDAOImplDb) gastoDAO).setHibernateSession(session);
            gastos = gastoDAO.obtenerGastosPorUsuarioYPeriodo(idUsuario, desde, hasta);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener gastos del periodo: " + e.getMessage());
        } finally {
            session.close();
        }
        return gastos;
    }

    public List<Gasto> obtenerGastosPendientesPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Gasto> gastos;
        try {
            ((GastoDAOImplDb) gastoDAO).setHibernateSession(session);
            gastos = gastoDAO.obtenerGastosPendientesPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener gastos pendientes: " + e.getMessage());
        } finally {
            session.close();
        }
        return gastos;
    }

    public List<Gasto> obtenerTodosLosGastos() throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Gasto> gastos;
        try {
            ((GastoDAOImplDb) gastoDAO).setHibernateSession(session);
            gastos = gastoDAO.obtenerTodosLosGastos();
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener todos los gastos: " + e.getMessage());
        } finally {
            session.close();
        }
        return gastos;
    }

    public boolean editarGasto(int idGasto, String descripcion, int idCategoria, double valor, int idMetodo, String estado) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((GastoDAOImplDb) gastoDAO).setHibernateSession(session);
            CategoriaConcepto categoria = session.get(CategoriaConcepto.class, idCategoria);
            MetodoOperacion metodo = session.get(MetodoOperacion.class, idMetodo);
            Gasto gasto = new Gasto(idGasto, null, null, descripcion, EstadoPago.valueOf(estado), categoria, valor, metodo);
            session.beginTransaction();
            exito = gastoDAO.editarGasto(gasto);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al editar gasto: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean cambiarEstadoGasto(int idGasto, String nuevoEstado) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((GastoDAOImplDb) gastoDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = gastoDAO.cambiarEstadoGasto(idGasto, nuevoEstado);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al cambiar estado del gasto: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }
}
