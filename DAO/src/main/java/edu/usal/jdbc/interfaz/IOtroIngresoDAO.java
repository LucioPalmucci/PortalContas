package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.OtroIngreso;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.Date;
import java.util.List;

public interface IOtroIngresoDAO {

    //Retornan OtroIngreso - tabla OtroIngreso
    OtroIngreso obtenerOtroIngresoPorId(int idOtroIngreso) throws HQLException;

    //Retornan List<OtroIngreso> - tabla OtroIngreso
    List<OtroIngreso> obtenerOtrosIngresosPorUsuario(int idUsuario) throws HQLException;

    List<OtroIngreso> obtenerOtrosIngresosPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException;

    List<OtroIngreso> obtenerOtrosIngresosPendientesPorUsuario(int idUsuario) throws HQLException;

    List<OtroIngreso> obtenerTodosLosOtrosIngresos() throws HQLException;

    //Retornan boolean - tabla OtroIngreso
    boolean guardarOtroIngreso(OtroIngreso otroIngreso) throws HQLException;

    boolean editarOtroIngreso(OtroIngreso otroIngreso) throws HQLException;

    boolean cambiarEstadoOtroIngreso(int idOtroIngreso, String nuevoEstado) throws HQLException;
}
