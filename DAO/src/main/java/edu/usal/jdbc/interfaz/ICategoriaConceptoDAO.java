package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.List;

public interface ICategoriaConceptoDAO {

    //Retornan CategoriaConcepto - tabla CategoriaConcepto
    CategoriaConcepto obtenerCategoriaPorId(int idCategoria) throws HQLException;

    //Retornan List<CategoriaConcepto> - tabla CategoriaConcepto
    List<CategoriaConcepto> obtenerTodasLasCategorias() throws HQLException;

    List<CategoriaConcepto> obtenerCategoriasPorAplicaA(String aplicaA) throws HQLException;

    //Retornan boolean - tabla CategoriaConcepto
    boolean guardarCategoria(CategoriaConcepto categoria) throws HQLException;

    boolean editarCategoria(CategoriaConcepto categoria) throws HQLException;

    boolean eliminarCategoria(CategoriaConcepto categoria) throws HQLException;
}
