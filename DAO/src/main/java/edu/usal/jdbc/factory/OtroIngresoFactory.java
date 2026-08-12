package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.OtroIngresoDAOImplDb;
import edu.usal.jdbc.interfaz.IOtroIngresoDAO;

public class OtroIngresoFactory {

    public static IOtroIngresoDAO getOtroIngresoDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new OtroIngresoDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
