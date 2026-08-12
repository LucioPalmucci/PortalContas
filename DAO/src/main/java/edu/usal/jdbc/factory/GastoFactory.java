package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.GastoDAOImplDb;
import edu.usal.jdbc.interfaz.IGastoDAO;

public class GastoFactory {

    public static IGastoDAO getGastoDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new GastoDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
