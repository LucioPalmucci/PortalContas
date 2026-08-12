package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.dominio.OtroEgreso;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.IOtroEgresoDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class OtroEgresoDAOImplDb implements IOtroEgresoDAO {

    private Session hybernateSession;

    public OtroEgresoDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public OtroEgreso obtenerOtroEgresoPorId(int idOtroEgreso) throws HQLException {
        try {
            return hybernateSession.get(OtroEgreso.class, idOtroEgreso);
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener el otro egreso por id: " + e.getMessage());
        }
    }

    @Override
    public List<OtroEgreso> obtenerOtrosEgresosPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<OtroEgreso> query = hybernateSession.createQuery(
                    "from OtroEgreso o where o.usuario.idUsuario = :idUsuario and o.estado <> :anulado order by o.fecha desc", OtroEgreso.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("anulado", EstadoPago.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los otros egresos del usuario: " + e.getMessage());
        }
    }

    @Override
    public List<OtroEgreso> obtenerOtrosEgresosPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException {
        try {
            Query<OtroEgreso> query = hybernateSession.createQuery(
                    "from OtroEgreso o where o.usuario.idUsuario = :idUsuario and o.fecha between :desde and :hasta and o.estado <> :anulado order by o.fecha desc",
                    OtroEgreso.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("desde", desde);
            query.setParameter("hasta", hasta);
            query.setParameter("anulado", EstadoPago.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los otros egresos del periodo: " + e.getMessage());
        }
    }

    @Override
    public List<OtroEgreso> obtenerOtrosEgresosPendientesPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<OtroEgreso> query = hybernateSession.createQuery(
                    "from OtroEgreso o where o.usuario.idUsuario = :idUsuario and o.estado = :pendiente order by o.fecha desc",
                    OtroEgreso.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("pendiente", EstadoPago.PENDIENTE_DE_PAGO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los otros egresos pendientes: " + e.getMessage());
        }
    }

    @Override
    public List<OtroEgreso> obtenerTodosLosOtrosEgresos() throws HQLException {
        try {
            Query<OtroEgreso> query = hybernateSession.createQuery(
                    "from OtroEgreso o where o.estado <> :anulado order by o.fecha desc", OtroEgreso.class);
            query.setParameter("anulado", EstadoPago.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener todos los otros egresos: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarOtroEgreso(OtroEgreso otroEgreso) throws HQLException {
        try {
            hybernateSession.save(otroEgreso);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar el otro egreso: " + e.getMessage());
        }
    }

    @Override
    public boolean editarOtroEgreso(OtroEgreso otroEgreso) throws HQLException {
        try {
            OtroEgreso existente = hybernateSession.get(OtroEgreso.class, otroEgreso.getIdOtroEgreso());
            if (existente == null) {
                return false;
            }
            existente.setDescripcion(otroEgreso.getDescripcion());
            existente.setEstado(otroEgreso.getEstado());
            existente.setCategoria(otroEgreso.getCategoria());
            existente.setValor(otroEgreso.getValor());
            existente.setMetodo(otroEgreso.getMetodo());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar el otro egreso: " + e.getMessage());
        }
    }

    @Override
    public boolean cambiarEstadoOtroEgreso(int idOtroEgreso, String nuevoEstado) throws HQLException {
        try {
            OtroEgreso existente = hybernateSession.get(OtroEgreso.class, idOtroEgreso);
            if (existente == null) {
                return false;
            }
            existente.setEstado(EstadoPago.valueOf(nuevoEstado));
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al cambiar el estado del otro egreso: " + e.getMessage());
        }
    }
}
