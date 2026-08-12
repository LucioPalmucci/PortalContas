package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.AplicaA;
import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.excepciones.HQLException;
import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.factory.CategoriaConceptoFactory;
import edu.usal.jdbc.implementacion.CategoriaConceptoDAOImplDb;
import edu.usal.jdbc.interfaz.ICategoriaConceptoDAO;
import edu.usal.jdbc.util.ConfigUtil;
import edu.usal.jdbc.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class CategoriaConceptoServicio {

    private final ICategoriaConceptoDAO categoriaConceptoDAO;

    public CategoriaConceptoServicio() {
        String fuente = ConfigUtil.getPropertyConfigInstance().getKey("DAO.categoriaConcepto");
        this.categoriaConceptoDAO = CategoriaConceptoFactory.getCategoriaConceptoDAO(fuente);
    }

    public CategoriaConcepto obtenerCategoriaPorId(int idCategoria) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CategoriaConcepto categoria;
        try {
            ((CategoriaConceptoDAOImplDb) categoriaConceptoDAO).setHibernateSession(session);
            categoria = categoriaConceptoDAO.obtenerCategoriaPorId(idCategoria);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener categoria por id: " + e.getMessage());
        } finally {
            session.close();
        }
        return categoria;
    }

    public List<CategoriaConcepto> obtenerTodasLasCategorias() throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CategoriaConcepto> categorias;
        try {
            ((CategoriaConceptoDAOImplDb) categoriaConceptoDAO).setHibernateSession(session);
            categorias = categoriaConceptoDAO.obtenerTodasLasCategorias();
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener todas las categorias: " + e.getMessage());
        } finally {
            session.close();
        }
        return categorias;
    }

    public List<CategoriaConcepto> obtenerCategoriasPorAplicaA(String aplicaA) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CategoriaConcepto> categorias;
        try {
            ((CategoriaConceptoDAOImplDb) categoriaConceptoDAO).setHibernateSession(session);
            categorias = categoriaConceptoDAO.obtenerCategoriasPorAplicaA(aplicaA);
        } catch (HQLException e) {
            throw new ServiceException("Error al obtener categorias por aplicaA: " + e.getMessage());
        } finally {
            session.close();
        }
        return categorias;
    }

    public boolean guardarCategoria(String nombre, String aplicaA) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((CategoriaConceptoDAOImplDb) categoriaConceptoDAO).setHibernateSession(session);
            CategoriaConcepto categoria = new CategoriaConcepto(nombre, AplicaA.valueOf(aplicaA));
            session.beginTransaction();
            exito = categoriaConceptoDAO.guardarCategoria(categoria);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al guardar categoria: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean editarCategoria(int idCategoria, String nombre, String aplicaA) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            ((CategoriaConceptoDAOImplDb) categoriaConceptoDAO).setHibernateSession(session);
            CategoriaConcepto categoria = new CategoriaConcepto(idCategoria, nombre, AplicaA.valueOf(aplicaA));
            session.beginTransaction();
            exito = categoriaConceptoDAO.editarCategoria(categoria);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al editar categoria: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }

    public boolean eliminarCategoria(int idCategoria) throws ServiceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean exito;
        try {
            CategoriaConcepto categoria = new CategoriaConcepto();
            categoria.setIdCategoria(idCategoria);
            ((CategoriaConceptoDAOImplDb) categoriaConceptoDAO).setHibernateSession(session);
            session.beginTransaction();
            exito = categoriaConceptoDAO.eliminarCategoria(categoria);
            if (exito) session.getTransaction().commit();
            else session.getTransaction().rollback();
        } catch (HQLException e) {
            session.getTransaction().rollback();
            throw new ServiceException("Error al eliminar categoria: " + e.getMessage());
        } finally {
            session.close();
        }
        return exito;
    }
}
