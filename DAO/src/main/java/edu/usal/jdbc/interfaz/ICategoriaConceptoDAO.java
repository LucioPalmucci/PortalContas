package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.List;

public interface ICategoriaConceptoDAO {

    CategoriaConcepto obtenerCategoriaPorId(int idCategoria) throws HQLException;

    List<CategoriaConcepto> obtenerTodasLasCategorias() throws HQLException;

    List<CategoriaConcepto> obtenerCategoriasPorAplicaA(String aplicaA) throws HQLException;

    boolean guardarCategoria(CategoriaConcepto categoria) throws HQLException;

    boolean editarCategoria(CategoriaConcepto categoria) throws HQLException;

    boolean cambiarEstadoCategoria(int idCategoria, boolean estaActivo) throws HQLException;
}
