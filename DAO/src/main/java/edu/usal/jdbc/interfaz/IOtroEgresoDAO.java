package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.OtroEgreso;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.Date;
import java.util.List;

public interface IOtroEgresoDAO {

    //Retornan OtroEgreso - tabla OtroEgreso
    OtroEgreso obtenerOtroEgresoPorId(int idOtroEgreso) throws HQLException;

    //Retornan List<OtroEgreso> - tabla OtroEgreso
    List<OtroEgreso> obtenerOtrosEgresosPorUsuario(int idUsuario) throws HQLException;

    List<OtroEgreso> obtenerOtrosEgresosPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException;

    List<OtroEgreso> obtenerOtrosEgresosPendientesPorUsuario(int idUsuario) throws HQLException;

    List<OtroEgreso> obtenerTodosLosOtrosEgresos() throws HQLException;

    //Retornan boolean - tabla OtroEgreso
    boolean guardarOtroEgreso(OtroEgreso otroEgreso) throws HQLException;

    boolean editarOtroEgreso(OtroEgreso otroEgreso) throws HQLException;

    boolean cambiarEstadoOtroEgreso(int idOtroEgreso, String nuevoEstado) throws HQLException;
}
