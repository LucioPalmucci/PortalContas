package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.ConfiguracionEstadoResultados;
import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.ConfiguracionEstadoResultadosFactory;
import edu.usal.jdbc.implementacion.ConfiguracionEstadoResultadosDAOImplDb;
import edu.usal.jdbc.interfaz.IConfiguracionEstadoResultadosDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConfiguracionServicio {

    private final IConfiguracionEstadoResultadosDAO configuracionDAO;

    public ConfiguracionServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.configuracion");
        this.configuracionDAO = ConfiguracionEstadoResultadosFactory.getConfiguracionEstadoResultadosDAO(fuente);
    }

    public ConfiguracionEstadoResultados obtenerConfiguracionPorUsuario(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ConfiguracionEstadoResultados configuracion;
        try {
            ((ConfiguracionEstadoResultadosDAOImplDb) configuracionDAO).setHibernateSession(session);
            configuracion = configuracionDAO.obtenerConfiguracionPorUsuario(idUsuario);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener la configuracion del usuario: " + e.getMessage());
        } finally {
            session.close();
        }
        return configuracion;
    }

    //Si el usuario todavia no tiene configuracion, crea una con valores por defecto (mes actual, sin categorias excluidas)
    public ConfiguracionEstadoResultados obtenerOCrearConfiguracion(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ConfiguracionEstadoResultados configuracion;
        try {
            ((ConfiguracionEstadoResultadosDAOImplDb) configuracionDAO).setHibernateSession(session);
            configuracion = configuracionDAO.obtenerConfiguracionPorUsuario(idUsuario);
            if (configuracion == null) {
                Usuario usuario = session.get(Usuario.class, idUsuario);
                configuracion = new ConfiguracionEstadoResultados(usuario, primerDiaDelMesActual(), new Date(), 0.0, new ArrayList<>());
                session.beginTransaction();
                boolean exito = configuracionDAO.guardarConfiguracion(configuracion);
                if (exito) session.getTransaction().commit();
                else session.getTransaction().rollback();
            }
        } catch (HQLException e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) session.getTransaction().rollback();
            throw new ServiceException("Error al obtener o crear la configuracion: " + e.getMessage());
        } finally {
            session.close();
        }
        return configuracion;
    }

    public boolean resetearConfiguracion(int idUsuario) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((ConfiguracionEstadoResultadosDAOImplDb) configuracionDAO).setHibernateSession(session);
            ConfiguracionEstadoResultados configuracion = configuracionDAO.obtenerConfiguracionPorUsuario(idUsuario);
            if (configuracion == null) {
                return false;
            }
            configuracion.setPeriodoInicioDefault(primerDiaDelMesActual());
            configuracion.setPeriodoFinDefault(new Date());
            configuracion.setMargenCMV(0.0);
            session.beginTransaction();
            exito = configuracionDAO.editarConfiguracion(configuracion);
            if (exito) exito = configuracionDAO.actualizarCategoriasExcluidas(configuracion.getIdConfiguracion(), new ArrayList<>());
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al resetear la configuracion: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    //Crea la configuracion del usuario si no existe, o la actualiza si ya existe (upsert)
    public boolean guardarOActualizarConfiguracion(int idUsuario, Date periodoInicio, Date periodoFin, double margenCMV, List<Integer> idsCategoriasExcluidas) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((ConfiguracionEstadoResultadosDAOImplDb) configuracionDAO).setHibernateSession(session);
            ConfiguracionEstadoResultados configuracion = configuracionDAO.obtenerConfiguracionPorUsuario(idUsuario);
            session.beginTransaction();
            if (configuracion == null) {
                Usuario usuario = session.get(Usuario.class, idUsuario);
                configuracion = new ConfiguracionEstadoResultados(usuario, periodoInicio, periodoFin, margenCMV, new ArrayList<>());
                exito = configuracionDAO.guardarConfiguracion(configuracion);
                if (exito && !idsCategoriasExcluidas.isEmpty()) {
                    exito = configuracionDAO.actualizarCategoriasExcluidas(configuracion.getIdConfiguracion(), idsCategoriasExcluidas);
                }
            } else {
                configuracion.setPeriodoInicioDefault(periodoInicio);
                configuracion.setPeriodoFinDefault(periodoFin);
                configuracion.setMargenCMV(margenCMV);
                exito = configuracionDAO.editarConfiguracion(configuracion);
                if (exito) exito = configuracionDAO.actualizarCategoriasExcluidas(configuracion.getIdConfiguracion(), idsCategoriasExcluidas);
            }
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al guardar o actualizar la configuracion: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    private Date primerDiaDelMesActual() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
