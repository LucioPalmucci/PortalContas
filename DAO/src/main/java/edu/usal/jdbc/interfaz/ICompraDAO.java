package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.Compra;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.Date;
import java.util.List;

public interface ICompraDAO {

    //Retornan Compra - tabla Compra
    Compra obtenerCompraPorId(int idCompra) throws HQLException;

    //Retornan List<Compra> - tabla Compra
    List<Compra> obtenerComprasPorUsuario(int idUsuario) throws HQLException;

    List<Compra> obtenerComprasPorUsuarioYPeriodo(int idUsuario, Date desde, Date hasta) throws HQLException;

    List<Compra> obtenerComprasPendientesPorUsuario(int idUsuario) throws HQLException;

    List<Compra> obtenerTodasLasCompras() throws HQLException;

    //Retornan boolean - tabla Compra
    boolean guardarCompra(Compra compra) throws HQLException;

    boolean editarCompra(Compra compra) throws HQLException;

    boolean cambiarEstadoCompra(int idCompra, String nuevoEstado) throws HQLException;
}
