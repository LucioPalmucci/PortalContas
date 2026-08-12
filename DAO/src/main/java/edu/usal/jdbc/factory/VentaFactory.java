package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.VentaDAOImplDb;
import edu.usal.jdbc.interfaz.IVentaDAO;

public class VentaFactory {

    public static IVentaDAO getVentaDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new VentaDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
