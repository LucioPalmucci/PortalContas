package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.CompraDAOImplDb;
import edu.usal.jdbc.interfaz.ICompraDAO;

public class CompraFactory {

    public static ICompraDAO getCompraDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new CompraDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
