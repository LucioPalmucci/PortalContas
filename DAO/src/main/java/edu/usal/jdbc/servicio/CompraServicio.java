package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.Compra;
import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.CompraFactory;
import edu.usal.jdbc.implementacion.CompraDAOImplDb;
import edu.usal.jdbc.interfaz.ICompraDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class CompraServicio {

    private final ICompraDAO compraDAO;

    public CompraServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.compra");
        this.compraDAO = CompraFactory.getCompraDAO(fuente);
    }

    public boolean crearCompra(int idUsuario, Date fecha, String descripcion, String conceptoCompra, double precioUnitario, int cantidad, int idMetodo, String estadoInicial) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((CompraDAOImplDb) compraDAO).setHibernateSession(session);
            Usuario usuario = session.get(Usuario.class, idUsuario);
            MetodoOperacion metodo = session.get(MetodoOperacion.class, idMetodo);
            double valorTotal = precioUnitario * cantidad;
            Compra compraACrear = new Compra(usuario, fecha, descripcion, EstadoPago.valueOf(estadoInicial), conceptoCompra, precioUnitario, cantidad, metodo, valorTotal);
            session.beginTransaction();
            exito = compraDAO.guardarCompra(compraACrear);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al crear compra: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public Compra obtenerCompraPorId(int idCompra) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Compra compra;
        try {
            ((CompraDAOImplDb) compraDAO).setHibernateSession(session);
            compra = compraDAO.obtenerCompraPorId(idCompra);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener compra por id: " + e.getMessage());
        } finally {
            session.close();
        }
        return compra;
    }

    public List<Compra> obtenerComprasPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Compra> compras;
        try {
            ((CompraDAOImplDb) compraDAO).setHibernateSession(session);
            compras = compraDAO.obtenerComprasPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener compras del usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return compras;
    }

    public List<Compra> obtenerComprasPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Compra> compras;
        try {
            ((CompraDAOImplDb) compraDAO).setHibernateSession(session);
            compras = compraDAO.obtenerComprasPorUsuarioYPeriodo(idUsuario, desde, hasta);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener compras del usuario por periodo: " + e.getMessage());
        } finally {
            session.close();
        }
        return compras;
    }

    public List<Compra> obtenerComprasPendientesPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Compra> compras;
        try {
            ((CompraDAOImplDb) compraDAO).setHibernateSession(session);
            compras = compraDAO.obtenerComprasPendientesPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener compras pendientes del usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return compras;
    }

    public List<Compra> obtenerTodasLasCompras() throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Compra> compras;
        try {
            ((CompraDAOImplDb) compraDAO).setHibernateSession(session);
            compras = compraDAO.obtenerTodasLasCompras();
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener todas las compras: " + e.getMessage());
        } finally {
            session.close();
        }
        return compras;
    }

    public boolean editarCompra(Compra compra) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((CompraDAOImplDb) compraDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = compraDAO.editarCompra(compra);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al editar compra: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean cambiarEstadoCompra(int idCompra, String nuevoEstado) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((CompraDAOImplDb) compraDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = compraDAO.cambiarEstadoCompra(idCompra, nuevoEstado);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al cambiar el estado de la compra: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }
}
