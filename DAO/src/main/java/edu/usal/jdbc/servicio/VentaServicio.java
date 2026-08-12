package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.EstadoCobro;
import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.dominio.Venta;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.VentaFactory;
import edu.usal.jdbc.implementacion.VentaDAOImplDb;
import edu.usal.jdbc.interfaz.IVentaDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class VentaServicio {

    private final IVentaDAO ventaDAO;

    public VentaServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.venta");
        this.ventaDAO = VentaFactory.getVentaDAO(fuente);
    }

    public boolean crearVenta(int idUsuario, Date fecha, String descripcion, String conceptoVenta, double precioUnitario, int cantidad, int idMetodo, String estadoInicial) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((VentaDAOImplDb) ventaDAO).setHibernateSession(session);
            Usuario usuario = session.get(Usuario.class, idUsuario);
            MetodoOperacion metodo = session.get(MetodoOperacion.class, idMetodo);
            double subtotal = precioUnitario * cantidad;
            Venta ventaACrear = new Venta(usuario, fecha, descripcion, EstadoCobro.valueOf(estadoInicial), conceptoVenta, precioUnitario, cantidad, metodo, subtotal);
            session.beginTransaction();
            exito = ventaDAO.guardarVenta(ventaACrear);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al crear venta: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public Venta obtenerVentaPorId(int idVenta) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Venta venta;
        try {
            ((VentaDAOImplDb) ventaDAO).setHibernateSession(session);
            venta = ventaDAO.obtenerVentaPorId(idVenta);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener venta por id: " + e.getMessage());
        } finally {
            session.close();
        }
        return venta;
    }

    public List<Venta> obtenerVentasPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Venta> ventas;
        try {
            ((VentaDAOImplDb) ventaDAO).setHibernateSession(session);
            ventas = ventaDAO.obtenerVentasPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener ventas del usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return ventas;
    }

    public List<Venta> obtenerVentasPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Venta> ventas;
        try {
            ((VentaDAOImplDb) ventaDAO).setHibernateSession(session);
            ventas = ventaDAO.obtenerVentasPorUsuarioYPeriodo(idUsuario, desde, hasta);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener ventas del usuario por periodo: " + e.getMessage());
        } finally {
            session.close();
        }
        return ventas;
    }

    public List<Venta> obtenerVentasPendientesPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Venta> ventas;
        try {
            ((VentaDAOImplDb) ventaDAO).setHibernateSession(session);
            ventas = ventaDAO.obtenerVentasPendientesPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener ventas pendientes del usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return ventas;
    }

    public List<Venta> obtenerTodasLasVentas() throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Venta> ventas;
        try {
            ((VentaDAOImplDb) ventaDAO).setHibernateSession(session);
            ventas = ventaDAO.obtenerTodasLasVentas();
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener todas las ventas: " + e.getMessage());
        } finally {
            session.close();
        }
        return ventas;
    }

    public boolean editarVenta(Venta venta) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((VentaDAOImplDb) ventaDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = ventaDAO.editarVenta(venta);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al editar venta: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean cambiarEstadoVenta(int idVenta, String nuevoEstado) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((VentaDAOImplDb) ventaDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = ventaDAO.cambiarEstadoVenta(idVenta, nuevoEstado);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al cambiar el estado de la venta: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }
}
