package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.MetodoOperacionDAOImplDb;
import edu.usal.jdbc.interfaz.IMetodoOperacionDAO;

public class MetodoOperacionFactory {

    public static IMetodoOperacionDAO getMetodoOperacionDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new MetodoOperacionDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
