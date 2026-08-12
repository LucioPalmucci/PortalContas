package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.dominio.Gasto;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.IGastoDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class GastoDAOImplDb implements IGastoDAO {

    private Session hybernateSession;

    public GastoDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public Gasto obtenerGastoPorId(int idGasto) throws HQLException {
        try {
            return hybernateSession.get(Gasto.class, idGasto);
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener el gasto por id: " + e.getMessage());
        }
    }

    @Override
    public List<Gasto> obtenerGastosPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<Gasto> query = hybernateSession.createQuery(
                    "from Gasto g where g.usuario.idUsuario = :idUsuario and g.estado <> :anulado order by g.fecha desc", Gasto.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("anulado", EstadoPago.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los gastos del usuario: " + e.getMessage());
        }
    }

    @Override
    public List<Gasto> obtenerGastosPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException {
        try {
            Query<Gasto> query = hybernateSession.createQuery(
                    "from Gasto g where g.usuario.idUsuario = :idUsuario and g.fecha between :desde and :hasta and g.estado <> :anulado order by g.fecha desc",
                    Gasto.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("desde", desde);
            query.setParameter("hasta", hasta);
            query.setParameter("anulado", EstadoPago.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los gastos del periodo: " + e.getMessage());
        }
    }

    @Override
    public List<Gasto> obtenerGastosPendientesPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<Gasto> query = hybernateSession.createQuery(
                    "from Gasto g where g.usuario.idUsuario = :idUsuario and g.estado = :pendiente order by g.fecha desc",
                    Gasto.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("pendiente", EstadoPago.PENDIENTE_DE_PAGO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los gastos pendientes: " + e.getMessage());
        }
    }

    @Override
    public List<Gasto> obtenerTodosLosGastos() throws HQLException {
        try {
            Query<Gasto> query = hybernateSession.createQuery(
                    "from Gasto g where g.estado <> :anulado order by g.fecha desc", Gasto.class);
            query.setParameter("anulado", EstadoPago.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener todos los gastos: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarGasto(Gasto gasto) throws HQLException {
        try {
            hybernateSession.save(gasto);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar el gasto: " + e.getMessage());
        }
    }

    @Override
    public boolean editarGasto(Gasto gasto) throws HQLException {
        try {
            Gasto existente = hybernateSession.get(Gasto.class, gasto.getIdGasto());
            if (existente == null) {
                return false;
            }
            existente.setDescripcion(gasto.getDescripcion());
            existente.setEstado(gasto.getEstado());
            existente.setCategoria(gasto.getCategoria());
            existente.setValor(gasto.getValor());
            existente.setMetodo(gasto.getMetodo());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar el gasto: " + e.getMessage());
        }
    }

    @Override
    public boolean cambiarEstadoGasto(int idGasto, String nuevoEstado) throws HQLException {
        try {
            Gasto existente = hybernateSession.get(Gasto.class, idGasto);
            if (existente == null) {
                return false;
            }
            existente.setEstado(EstadoPago.valueOf(nuevoEstado));
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al cambiar el estado del gasto: " + e.getMessage());
        }
    }
}
