package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.EstadoVencimiento;
import edu.usal.jdbc.dominio.Vencimiento;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.IVencimientoDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class VencimientoDAOImplDb implements IVencimientoDAO {

    private Session hybernateSession;

    public VencimientoDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public Vencimiento obtenerVencimientoPorId(int idVencimiento) throws HQLException {
        try {
            return hybernateSession.get(Vencimiento.class, idVencimiento);
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener el vencimiento por id: " + e.getMessage());
        }
    }

    @Override
    public List<Vencimiento> obtenerTodosLosVencimientos() throws HQLException {
        try {
            Query<Vencimiento> query = hybernateSession.createQuery(
                    "from Vencimiento v order by v.fecha asc", Vencimiento.class);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener todos los vencimientos: " + e.getMessage());
        }
    }

    @Override
    public List<Vencimiento> obtenerVencimientosPorEstado(String estado) throws HQLException {
        try {
            Query<Vencimiento> query = hybernateSession.createQuery(
                    "from Vencimiento v where v.estado = :estado order by v.fecha asc", Vencimiento.class);
            query.setParameter("estado", EstadoVencimiento.valueOf(estado));
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los vencimientos por estado: " + e.getMessage());
        }
    }

    @Override
    public List<Vencimiento> obtenerVencimientosPorRangoFechas(Date desde, Date hasta) throws HQLException {
        try {
            Query<Vencimiento> query = hybernateSession.createQuery(
                    "from Vencimiento v where v.fecha between :desde and :hasta order by v.fecha asc", Vencimiento.class);
            query.setParameter("desde", desde);
            query.setParameter("hasta", hasta);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener los vencimientos por rango de fechas: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarVencimiento(Vencimiento vencimiento) throws HQLException {
        try {
            hybernateSession.save(vencimiento);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar el vencimiento: " + e.getMessage());
        }
    }

    @Override
    public boolean editarVencimiento(Vencimiento vencimiento) throws HQLException {
        try {
            Vencimiento existente = hybernateSession.get(Vencimiento.class, vencimiento.getIdVencimiento());
            if (existente == null) {
                return false;
            }
            existente.setFecha(vencimiento.getFecha());
            existente.setUltimosDigitosCuit(vencimiento.getUltimosDigitosCuit());
            existente.setNombreCliente(vencimiento.getNombreCliente());
            existente.setImpuestoPagar(vencimiento.getImpuestoPagar());
            existente.setEstado(vencimiento.getEstado());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar el vencimiento: " + e.getMessage());
        }
    }

    @Override
    public boolean marcarRealizado(int idVencimiento) throws HQLException {
        try {
            Vencimiento existente = hybernateSession.get(Vencimiento.class, idVencimiento);
            if (existente == null) {
                return false;
            }
            existente.setEstado(EstadoVencimiento.REALIZADO);
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al marcar el vencimiento como realizado: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarVencimientoFisico(int idVencimiento) throws HQLException {
        try {
            Vencimiento existente = hybernateSession.get(Vencimiento.class, idVencimiento);
            if (existente == null) {
                return false;
            }
            hybernateSession.delete(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al eliminar el vencimiento: " + e.getMessage());
        }
    }
}
