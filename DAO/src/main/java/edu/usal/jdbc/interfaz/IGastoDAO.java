package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.Gasto;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.Date;
import java.util.List;

public interface IGastoDAO {

    //Retornan Gasto - tabla Gasto
    Gasto obtenerGastoPorId(int idGasto) throws HQLException;

    //Retornan List<Gasto> - tabla Gasto
    List<Gasto> obtenerGastosPorUsuario(int idUsuario) throws HQLException;

    List<Gasto> obtenerGastosPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException;

    List<Gasto> obtenerGastosPendientesPorUsuario(int idUsuario) throws HQLException;

    List<Gasto> obtenerTodosLosGastos() throws HQLException;

    //Retornan boolean - tabla Gasto
    boolean guardarGasto(Gasto gasto) throws HQLException;

    boolean editarGasto(Gasto gasto) throws HQLException;

    boolean cambiarEstadoGasto(int idGasto, String nuevoEstado) throws HQLException;
}
