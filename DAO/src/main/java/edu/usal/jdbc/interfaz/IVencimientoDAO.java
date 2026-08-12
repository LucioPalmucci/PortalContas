package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.Vencimiento;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.Date;
import java.util.List;

public interface IVencimientoDAO {

    //Retornan Vencimiento - tabla Vencimiento
    Vencimiento obtenerVencimientoPorId(int idVencimiento) throws HQLException;

    //Retornan List<Vencimiento> - tabla Vencimiento
    List<Vencimiento> obtenerTodosLosVencimientos() throws HQLException;

    List<Vencimiento> obtenerVencimientosPorEstado(String estado) throws HQLException;

    List<Vencimiento> obtenerVencimientosPorRangoFechas(Date desde, Date hasta) throws HQLException;

    //Retornan boolean - tabla Vencimiento
    boolean guardarVencimiento(Vencimiento vencimiento) throws HQLException;

    boolean editarVencimiento(Vencimiento vencimiento) throws HQLException;

    boolean marcarRealizado(int idVencimiento) throws HQLException;

    boolean eliminarVencimientoFisico(int idVencimiento) throws HQLException;
}
