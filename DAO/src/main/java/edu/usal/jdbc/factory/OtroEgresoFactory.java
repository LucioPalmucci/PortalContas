package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.OtroEgresoDAOImplDb;
import edu.usal.jdbc.interfaz.IOtroEgresoDAO;

public class OtroEgresoFactory {

    public static IOtroEgresoDAO getOtroEgresoDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new OtroEgresoDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
