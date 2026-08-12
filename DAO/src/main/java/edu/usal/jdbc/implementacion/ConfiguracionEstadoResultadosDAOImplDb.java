package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.dominio.ConfiguracionEstadoResultados;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.IConfiguracionEstadoResultadosDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionEstadoResultadosDAOImplDb implements IConfiguracionEstadoResultadosDAO {

    private Session hybernateSession;

    public ConfiguracionEstadoResultadosDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public ConfiguracionEstadoResultados obtenerConfiguracionPorUsuario(int idUsuario) throws HQLException {
        try {
            Query<ConfiguracionEstadoResultados> query = hybernateSession.createQuery(
                    "from ConfiguracionEstadoResultados c left join fetch c.categoriasExcluidas where c.usuario.idUsuario = :idUsuario",
                    ConfiguracionEstadoResultados.class);
            query.setParameter("idUsuario", idUsuario);
            return query.uniqueResult();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener la configuracion del usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarConfiguracion(ConfiguracionEstadoResultados config) throws HQLException {
        try {
            hybernateSession.save(config);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar la configuracion: " + e.getMessage());
        }
    }

    @Override
    public boolean editarConfiguracion(ConfiguracionEstadoResultados config) throws HQLException {
        try {
            ConfiguracionEstadoResultados existente = hybernateSession.get(ConfiguracionEstadoResultados.class, config.getIdConfiguracion());
            if (existente == null) {
                return false;
            }
            existente.setPeriodoInicioDefault(config.getPeriodoInicioDefault());
            existente.setPeriodoFinDefault(config.getPeriodoFinDefault());
            existente.setMargenCMV(config.getMargenCMV());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar la configuracion: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizarCategoriasExcluidas(int idConfiguracion, List<Integer> idsCategorias) throws HQLException {
        try {
            ConfiguracionEstadoResultados configuracion = hybernateSession.get(ConfiguracionEstadoResultados.class, idConfiguracion);
            if (configuracion == null) {
                return false;
            }
            List<CategoriaConcepto> categoriasExcluidas = new ArrayList<>();
            for (Integer idCategoria : idsCategorias) {
                CategoriaConcepto categoria = hybernateSession.get(CategoriaConcepto.class, idCategoria);
                if (categoria != null) {
                    categoriasExcluidas.add(categoria);
                }
            }
            configuracion.setCategoriasExcluidas(categoriasExcluidas);
            hybernateSession.update(configuracion);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al actualizar las categorias excluidas: " + e.getMessage());
        }
    }
}
