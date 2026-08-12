package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.VencimientoDAOImplDb;
import edu.usal.jdbc.interfaz.IVencimientoDAO;

public class VencimientoFactory {

    public static IVencimientoDAO getVencimientoDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new VencimientoDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
