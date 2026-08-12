package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.MetodoOperacion;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.List;

public interface IMetodoOperacionDAO {

    //Retornan MetodoOperacion - tabla MetodoOperacion
    MetodoOperacion obtenerMetodoPorId(int idMetodo) throws HQLException;

    //Retornan List<MetodoOperacion> - tabla MetodoOperacion
    List<MetodoOperacion> obtenerTodosLosMetodos() throws HQLException;

    List<MetodoOperacion> obtenerMetodosPorTipo(String cobroOPago) throws HQLException;

    //Retornan boolean - tabla MetodoOperacion
    boolean guardarMetodo(MetodoOperacion metodo) throws HQLException;

    boolean editarMetodo(MetodoOperacion metodo) throws HQLException;

    boolean eliminarMetodo(MetodoOperacion metodo) throws HQLException;
}
