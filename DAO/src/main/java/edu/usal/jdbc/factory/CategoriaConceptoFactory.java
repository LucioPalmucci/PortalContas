package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.CategoriaConceptoDAOImplDb;
import edu.usal.jdbc.interfaz.ICategoriaConceptoDAO;

public class CategoriaConceptoFactory {

    public static ICategoriaConceptoDAO getCategoriaConceptoDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new CategoriaConceptoDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
