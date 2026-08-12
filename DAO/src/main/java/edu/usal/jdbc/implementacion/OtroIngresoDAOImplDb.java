package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.EstadoCobro;
import edu.usal.jdbc.dominio.OtroIngreso;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.IOtroIngresoDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class OtroIngresoDAOImplDb implements IOtroIngresoDAO {

    private Session hybernateSession;

    public OtroIngresoDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public OtroIngreso obtenerOtroIngresoPorId(int idOtroIngreso) throws HQLException {
        try {
            return hybernateSession.get(OtroIngreso.class, idOtroIngreso);
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener el otro ingreso por id: " + e.getMessage());
        }
    }

    @Override
    public List<OtroIngreso> obtenerOtrosIngresosPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<OtroIngreso> query = hybernateSession.createQuery(
                    "from OtroIngreso o where o.usuario.idUsuario = :idUsuario and o.estado <> :anulado order by o.fecha desc", OtroIngreso.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("anulado", EstadoCobro.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los otros ingresos del usuario: " + e.getMessage());
        }
    }

    @Override
    public List<OtroIngreso> obtenerOtrosIngresosPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException {
        try {
            Query<OtroIngreso> query = hybernateSession.createQuery(
                    "from OtroIngreso o where o.usuario.idUsuario = :idUsuario and o.fecha between :desde and :hasta and o.estado <> :anulado order by o.fecha desc",
                    OtroIngreso.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("desde", desde);
            query.setParameter("hasta", hasta);
            query.setParameter("anulado", EstadoCobro.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los otros ingresos del periodo: " + e.getMessage());
        }
    }

    @Override
    public List<OtroIngreso> obtenerOtrosIngresosPendientesPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<OtroIngreso> query = hybernateSession.createQuery(
                    "from OtroIngreso o where o.usuario.idUsuario = :idUsuario and o.estado = :pendiente order by o.fecha desc",
                    OtroIngreso.class);
            query.setParameter("idUsuario", idUsuario);
            query.setParameter("pendiente", EstadoCobro.PENDIENTE_DE_COBRO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los otros ingresos pendientes: " + e.getMessage());
        }
    }

    @Override
    public List<OtroIngreso> obtenerTodosLosOtrosIngresos() throws HQLException {
        try {
            Query<OtroIngreso> query = hybernateSession.createQuery(
                    "from OtroIngreso o where o.estado <> :anulado order by o.fecha desc", OtroIngreso.class);
            query.setParameter("anulado", EstadoCobro.ANULADO);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener todos los otros ingresos: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarOtroIngreso(OtroIngreso otroIngreso) throws HQLException {
        try {
            hybernateSession.save(otroIngreso);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar el otro ingreso: " + e.getMessage());
        }
    }

    @Override
    public boolean editarOtroIngreso(OtroIngreso otroIngreso) throws HQLException {
        try {
            OtroIngreso existente = hybernateSession.get(OtroIngreso.class, otroIngreso.getIdOtroIngreso());
            if (existente == null) {
                return false;
            }
            existente.setDescripcion(otroIngreso.getDescripcion());
            existente.setEstado(otroIngreso.getEstado());
            existente.setCategoria(otroIngreso.getCategoria());
            existente.setValor(otroIngreso.getValor());
            existente.setMetodo(otroIngreso.getMetodo());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar el otro ingreso: " + e.getMessage());
        }
    }

    @Override
    public boolean cambiarEstadoOtroIngreso(int idOtroIngreso, String nuevoEstado) throws HQLException {
        try {
            OtroIngreso existente = hybernateSession.get(OtroIngreso.class, idOtroIngreso);
            if (existente == null) {
                return false;
            }
            existente.setEstado(EstadoCobro.valueOf(nuevoEstado));
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al cambiar el estado del otro ingreso: " + e.getMessage());
        }
    }
}
