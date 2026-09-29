package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.Vencimiento;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.Date;
import java.util.List;

public interface IVencimientoDAO {

    Vencimiento obtenerVencimientoPorId(int idVencimiento) throws HQLException;

    List<Vencimiento> obtenerTodosLosVencimientos() throws HQLException;

    boolean guardarVencimiento(Vencimiento vencimiento) throws HQLException;

    boolean editarVencimiento(Vencimiento vencimiento) throws HQLException;

    boolean marcarRealizado(int idVencimiento) throws HQLException;

    boolean eliminarVencimiento(int idVencimiento) throws HQLException;
}
