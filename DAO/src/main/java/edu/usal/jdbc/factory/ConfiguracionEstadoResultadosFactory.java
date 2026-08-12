package edu.usal.jdbc.factory;

import edu.usal.jdbc.excepciones.FactoryException;
import edu.usal.jdbc.implementacion.ConfiguracionEstadoResultadosDAOImplDb;
import edu.usal.jdbc.interfaz.IConfiguracionEstadoResultadosDAO;

public class ConfiguracionEstadoResultadosFactory {

    public static IConfiguracionEstadoResultadosDAO getConfiguracionEstadoResultadosDAO(String fuente) throws FactoryException {
        if (fuente.equalsIgnoreCase("hibernate")) {
            return new ConfiguracionEstadoResultadosDAOImplDb();
        }
        throw new FactoryException("Fuente de datos no soportada: " + fuente);
    }
}
