package edu.usal.jdbc.implementacion;

import edu.usal.jdbc.dominio.AplicaA;
import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.interfaz.ICategoriaConceptoDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CategoriaConceptoDAOImplDb implements ICategoriaConceptoDAO {

    private Session hybernateSession;

    public CategoriaConceptoDAOImplDb() {}

    public Boolean setHibernateSession(Session session) {
        this.hybernateSession = session;
        return true;
    }

    @Override
    public CategoriaConcepto obtenerCategoriaPorId(int idCategoria) throws HQLException {
        try {
            return hybernateSession.get(CategoriaConcepto.class, idCategoria);
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener la categoria por id: " + e.getMessage());
        }
    }

    @Override
    public List<CategoriaConcepto> obtenerTodasLasCategorias() throws HQLException {
        try {
            Query<CategoriaConcepto> query = hybernateSession.createQuery("from CategoriaConcepto", CategoriaConcepto.class);
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener todas las categorias: " + e.getMessage());
        }
    }

    @Override
    public List<CategoriaConcepto> obtenerCategoriasPorAplicaA(String aplicaA) throws HQLException {
        try {
            Query<CategoriaConcepto> query = hybernateSession.createQuery(
                    "from CategoriaConcepto c where c.aplicaA = :aplicaA", CategoriaConcepto.class);
            query.setParameter("aplicaA", AplicaA.valueOf(aplicaA));
            return query.list();
        } catch (HibernateException e) {
            throw new HQLException("Error al obtener categorias por aplicaA: " + e.getMessage());
        }
    }

    @Override
    public boolean guardarCategoria(CategoriaConcepto categoria) throws HQLException {
        try {
            hybernateSession.save(categoria);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al guardar la categoria: " + e.getMessage());
        }
    }

    @Override
    public boolean editarCategoria(CategoriaConcepto categoria) throws HQLException {
        try {
            CategoriaConcepto existente = hybernateSession.get(CategoriaConcepto.class, categoria.getIdCategoria());
            if (existente == null) {
                return false;
            }
            existente.setNombre(categoria.getNombre());
            existente.setAplicaA(categoria.getAplicaA());
            hybernateSession.update(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al editar la categoria: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarCategoria(CategoriaConcepto categoria) throws HQLException {
        try {
            CategoriaConcepto existente = hybernateSession.get(CategoriaConcepto.class, categoria.getIdCategoria());
            if (existente == null) {
                return false;
            }
            hybernateSession.delete(existente);
            return true;
        } catch (HibernateException e) {
            throw new HQLException("Error al eliminar la categoria: " + e.getMessage());
        }
    }
}
