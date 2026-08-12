package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.ConfiguracionEstadoResultados;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.List;

public interface IConfiguracionEstadoResultadosDAO {

    //Retornan ConfiguracionEstadoResultados - tabla ConfiguracionEstadoResultados
    ConfiguracionEstadoResultados obtenerConfiguracionPorUsuario(int idUsuario) throws HQLException;

    //Retornan boolean - tabla ConfiguracionEstadoResultados
    boolean guardarConfiguracion(ConfiguracionEstadoResultados config) throws HQLException;

    boolean editarConfiguracion(ConfiguracionEstadoResultados config) throws HQLException;

    //Retornan boolean - tabla ConfigCategoriaExcluida
    boolean actualizarCategoriasExcluidas(int idConfiguracion, List<Integer> idsCategorias) throws HQLException;
}
