package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.Venta;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.Date;
import java.util.List;

public interface IVentaDAO {

    //Retornan Venta - tabla Venta
    Venta obtenerVentaPorId(int idVenta) throws HQLException;

    //Retornan List<Venta> - tabla Venta
    List<Venta> obtenerVentasPorUsuario(int idUsuario) throws HQLException;

    List<Venta> obtenerVentasPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException;

    List<Venta> obtenerVentasPendientesPorUsuario(int idUsuario) throws HQLException;

    List<Venta> obtenerTodasLasVentas() throws HQLException;

    //Retornan boolean - tabla Venta
    boolean guardarVenta(Venta venta) throws HQLException;

    boolean editarVenta(Venta venta) throws HQLException;

    boolean cambiarEstadoVenta(int idVenta, String nuevoEstado) throws HQLException;
}
