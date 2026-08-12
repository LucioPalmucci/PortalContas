package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.UsuarioDAOImplDb;
import edu.usal.jdbc.interfaz.IUsuarioDAO;

public class UsuarioFactory {

    public static IUsuarioDAO getUsuarioDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new UsuarioDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
