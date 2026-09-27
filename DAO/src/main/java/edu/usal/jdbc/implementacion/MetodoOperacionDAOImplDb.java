package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.CobroOPago;
import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.IMetodoOperacionDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MetodoOperacionDAOImplDb implements IMetodoOperacionDAO {

    private Session hybernateSession;

    public MetodoOperacionDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public MetodoOperacion obtenerMetodoPorId(int idMetodo) throws HQLException {
        try {
            return hybernateSession.get(MetodoOperacion.class, idMetodo);
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener el metodo por id: " + e.getMessage());
        }
    }

    @Override
    public List<MetodoOperacion> obtenerTodosLosMetodos() throws HQLException {
        try {
            Query<MetodoOperacion> query = hybernateSession.createQuery("from MetodoOperacion", MetodoOperacion.class);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener todos los metodos: " + e.getMessage());
        }
    }

    @Override
    public List<MetodoOperacion> obtenerMetodosPorTipo(String cobroOPago) throws HQLException {
        try {
            Query<MetodoOperacion> query = hybernateSession.createQuery(
                    "from MetodoOperacion m where m.cobroOPago = :tipo and m.estaActivo = true", MetodoOperacion.class);
            query.setParameter("tipo", CobroOPago.valueOf(cobroOPago));
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener metodos por tipo: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarMetodo(MetodoOperacion metodo) throws HQLException {
        try {
            hybernateSession.save(metodo);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar el metodo: " + e.getMessage());
        }
    }

    @Override
    public boolean editarMetodo(MetodoOperacion metodo) throws HQLException {
        try {
            MetodoOperacion existente = hybernateSession.get(MetodoOperacion.class, metodo.getIdMetodo());
            if (existente == null) {
                return false;
            }
            existente.setNombre(metodo.getNombre());
            existente.setCobroOPago(metodo.getCobroOPago());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar el metodo: " + e.getMessage());
        }
    }

    // Baja logica: el medio se desactiva (deja de ofrecerse en los formularios) pero el registro se conserva.
    @Override
    public boolean cambiarEstadoMetodo(int idMetodo, boolean estaActivo) throws HQLException {
        try {
            MetodoOperacion existente = hybernateSession.get(MetodoOperacion.class, idMetodo);
            if (existente == null) {
                return false;
            }
            existente.setEstaActivo(estaActivo);
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al cambiar el estado del metodo: " + e.getMessage());
        }
    }
}
