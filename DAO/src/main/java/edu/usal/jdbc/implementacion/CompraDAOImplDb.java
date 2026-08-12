package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.Compra;
import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.ICompraDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class CompraDAOImplDb implements ICompraDAO {

    private Session hybernateSession;

    public CompraDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public Compra obtenerCompraPorId(int idCompra) throws HQLException {
        try {
            return hybernateSession.get(Compra.class, idCompra);
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener la compra por id: " + e.getMessage());
        }
    }

    @Override
    public List<Compra> obtenerComprasPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<Compra> query = hybernateSession.createQuery(
                    "from Compra c where c.usuario.idUsuario = :idUsuario and c.estado <> :anulado order by c.fecha desc", Compra.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("anulado", EstadoPago.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener las compras del usuario: " + e.getMessage());
        }
    }

    @Override
    public List<Compra> obtenerComprasPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException {
        try {
            Query<Compra> query = hybernateSession.createQuery(
                    "from Compra c where c.usuario.idUsuario = :idUsuario and c.fecha between :desde and :hasta and c.estado <> :anulado order by c.fecha desc",
                    Compra.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("desde", desde);
            query.setParameter("hasta", hasta);
            query.setParameter("anulado", EstadoPago.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener las compras del usuario por periodo: " + e.getMessage());
        }
    }

    @Override
    public List<Compra> obtenerComprasPendientesPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<Compra> query = hybernateSession.createQuery(
                    "from Compra c where c.usuario.idUsuario = :idUsuario and c.estado = :pendiente order by c.fecha desc",
                    Compra.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("pendiente", EstadoPago.PENDIENTE_DE_PAGO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener las compras pendientes del usuario: " + e.getMessage());
        }
    }

    @Override
    public List<Compra> obtenerTodasLasCompras() throws HQLException {
        try {
            Query<Compra> query = hybernateSession.createQuery(
                    "from Compra c where c.estado <> :anulado order by c.fecha desc", Compra.class);
            query.setParameter("anulado", EstadoPago.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener todas las compras: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarCompra(Compra compra) throws HQLException {
        try {
            hybernateSession.save(compra);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar la compra: " + e.getMessage());
        }
    }

    @Override
    public boolean editarCompra(Compra compra) throws HQLException {
        try {
            Compra existente = hybernateSession.get(Compra.class, compra.getIdCompra());
            if (existente == null) {
                return false;
            }
            existente.setDescripcion(compra.getDescripcion());
            existente.setEstado(compra.getEstado());
            existente.setConceptoCompra(compra.getConceptoCompra());
            existente.setPrecioUnitario(compra.getPrecioUnitario());
            existente.setCantidad(compra.getCantidad());
            existente.setMetodo(compra.getMetodo());
            existente.setValorTotal(compra.getPrecioUnitario() * compra.getCantidad());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar la compra: " + e.getMessage());
        }
    }

    @Override
    public boolean cambiarEstadoCompra(int idCompra, String nuevoEstado) throws HQLException {
        try {
            Compra existente = hybernateSession.get(Compra.class, idCompra);
            if (existente == null) {
                return false;
            }
            existente.setEstado(EstadoPago.valueOf(nuevoEstado));
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al cambiar el estado de la compra: " + e.getMessage());
        }
    }
}
