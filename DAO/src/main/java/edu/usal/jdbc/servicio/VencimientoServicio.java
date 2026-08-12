package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.EstadoVencimiento;
import edu.usal.jdbc.dominio.Vencimiento;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.VencimientoFactory;
import edu.usal.jdbc.implementacion.VencimientoDAOImplDb;
import edu.usal.jdbc.interfaz.IVencimientoDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class VencimientoServicio {

    private final IVencimientoDAO vencimientoDAO;

    public VencimientoServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.vencimiento");
        this.vencimientoDAO = VencimientoFactory.getVencimientoDAO(fuente);
    }

    public Vencimiento obtenerVencimientoPorId(int idVencimiento) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Vencimiento vencimiento;
        try {
            ((VencimientoDAOImplDb) vencimientoDAO).setHibernateSession(session);
            vencimiento = vencimientoDAO.obtenerVencimientoPorId(idVencimiento);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener el vencimiento por id: " + e.getMessage());
        } finally {
            session.close();
        }
        return vencimiento;
    }

    public List<Vencimiento> obtenerTodosLosVencimientos() throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vencimiento> vencimientos;
        try {
            ((VencimientoDAOImplDb) vencimientoDAO).setHibernateSession(session);
            vencimientos = vencimientoDAO.obtenerTodosLosVencimientos();
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener todos los vencimientos: " + e.getMessage());
        } finally {
            session.close();
        }
        return vencimientos;
    }

    public List<Vencimiento> obtenerVencimientosPorEstado(String estado) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vencimiento> vencimientos;
        try {
            ((VencimientoDAOImplDb) vencimientoDAO).setHibernateSession(session);
            vencimientos = vencimientoDAO.obtenerVencimientosPorEstado(estado);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener los vencimientos por estado: " + e.getMessage());
        } finally {
            session.close();
        }
        return vencimientos;
    }

    public List<Vencimiento> obtenerVencimientosPorRangoFechas(Date desde, Date hasta) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Vencimiento> vencimientos;
        try {
            ((VencimientoDAOImplDb) vencimientoDAO).setHibernateSession(session);
            vencimientos = vencimientoDAO.obtenerVencimientosPorRangoFechas(desde, hasta);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener los vencimientos por rango de fechas: " + e.getMessage());
        } finally {
            session.close();
        }
        return vencimientos;
    }

    public boolean guardarVencimiento(Vencimiento vencimiento) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((VencimientoDAOImplDb) vencimientoDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = vencimientoDAO.guardarVencimiento(vencimiento);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al guardar el vencimiento: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean editarVencimiento(Vencimiento vencimiento) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((VencimientoDAOImplDb) vencimientoDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = vencimientoDAO.editarVencimiento(vencimiento);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al editar el vencimiento: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean marcarRealizado(int idVencimiento) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((VencimientoDAOImplDb) vencimientoDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = vencimientoDAO.marcarRealizado(idVencimiento);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al marcar el vencimiento como realizado: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    //Unico caso de baja fisica de todo el sistema: el vencimiento no tiene relevancia historica una vez borrado
    public boolean eliminarVencimientoFisico(int idVencimiento) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((VencimientoDAOImplDb) vencimientoDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = vencimientoDAO.eliminarVencimientoFisico(idVencimiento);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al eliminar el vencimiento: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public int calcularDiasRestantes(Vencimiento vencimiento) {
        LocalDate fechaVencimiento = vencimiento.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    //PROXIMO es un estado derivado/visual, calculado a partir de PENDIENTE + dias restantes <= 7.
    //Nunca se persiste como EstadoVencimiento.PROXIMO en la base: los unicos estados almacenables son PENDIENTE y REALIZADO.
    public boolean esProximo(Vencimiento vencimiento) {
        return vencimiento.getEstado() == EstadoVencimiento.PENDIENTE
                && calcularDiasRestantes(vencimiento) >= 0
                && calcularDiasRestantes(vencimiento) <= 7;
    }
}
