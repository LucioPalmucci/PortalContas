package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.EstadoCobro;
import edu.usal.jdbc.dominio.Venta;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.IVentaDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class VentaDAOImplDb implements IVentaDAO {

    private Session hybernateSession;

    public VentaDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public Venta obtenerVentaPorId(int idVenta) throws HQLException {
        try {
            return hybernateSession.get(Venta.class, idVenta);
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener la venta por id: " + e.getMessage());
        }
    }

    @Override
    public List<Venta> obtenerVentasPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<Venta> query = hybernateSession.createQuery(
                    "from Venta v where v.usuario.idUsuario = :idUsuario and v.estado <> :anulado order by v.fecha desc", Venta.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("anulado", EstadoCobro.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener las ventas del usuario: " + e.getMessage());
        }
    }

    @Override
    public List<Venta> obtenerVentasPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException {
        try {
            Query<Venta> query = hybernateSession.createQuery(
                    "from Venta v where v.usuario.idUsuario = :idUsuario and v.fecha between :desde and :hasta and v.estado <> :anulado order by v.fecha desc",
                    Venta.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("desde", desde);
            query.setParameter("hasta", hasta);
            query.setParameter("anulado", EstadoCobro.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener las ventas del usuario por periodo: " + e.getMessage());
        }
    }

    @Override
    public List<Venta> obtenerVentasPendientesPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<Venta> query = hybernateSession.createQuery(
                    "from Venta v where v.usuario.idUsuario = :idUsuario and v.estado = :pendiente order by v.fecha desc",
                    Venta.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("pendiente", EstadoCobro.PENDIENTE_DE_COBRO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener las ventas pendientes del usuario: " + e.getMessage());
        }
    }

    @Override
    public List<Venta> obtenerTodasLasVentas() throws HQLException {
        try {
            Query<Venta> query = hybernateSession.createQuery(
                    "from Venta v where v.estado <> :anulado order by v.fecha desc", Venta.class);
            query.setParameter("anulado", EstadoCobro.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener todas las ventas: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarVenta(Venta venta) throws HQLException {
        try {
            hybernateSession.save(venta);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar la venta: " + e.getMessage());
        }
    }

    @Override
    public boolean editarVenta(Venta venta) throws HQLException {
        try {
            Venta existente = hybernateSession.get(Venta.class, venta.getIdVenta());
            if (existente == null) {
                return false;
            }
            existente.setDescripcion(venta.getDescripcion());
            existente.setEstado(venta.getEstado());
            existente.setConceptoVenta(venta.getConceptoVenta());
            existente.setPrecioUnitario(venta.getPrecioUnitario());
            existente.setCantidad(venta.getCantidad());
            existente.setMetodo(venta.getMetodo());
            existente.setSubtotal(venta.getPrecioUnitario() * venta.getCantidad());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar la venta: " + e.getMessage());
        }
    }

    @Override
    public boolean cambiarEstadoVenta(int idVenta, String nuevoEstado) throws HQLException {
        try {
            Venta existente = hybernateSession.get(Venta.class, idVenta);
            if (existente == null) {
                return false;
            }
            existente.setEstado(EstadoCobro.valueOf(nuevoEstado));
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al cambiar el estado de la venta: " + e.getMessage());
        }
    }
}
