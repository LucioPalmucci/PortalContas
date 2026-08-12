package edu.usal.jdbc.interfaz;

import edu.usal.jdbc.dominio.Usuario;
import edu.usal.jdbc.excepciones.HQLException;

import java.util.Date;
import java.util.List;

public interface IUsuarioDAO {

    //Retornan Usuario - tabla Usuario
    Usuario obtenerUsuarioPorId(int idUsuario) throws HQLException;

    Usuario obtenerUsuarioPorNombreUsuario(String nombreUsuario) throws HQLException;

    Usuario obtenerUsuarioPorCorreo(String correo) throws HQLException;

    //Retornan List<Usuario> - tabla Usuario
    List<Usuario> obtenerTodosLosUsuarios() throws HQLException;

    List<Usuario> buscarUsuarios(String texto) throws HQLException;

    //Retornan boolean - tabla Usuario
    boolean guardarUsuario(Usuario usuario) throws HQLException;

    boolean editarUsuario(Usuario usuario, int idUsuarioActor) throws HQLException;

    boolean actualizarContrasena(int idUsuario, String nuevaContrasenaHash) throws HQLException;

    boolean activarDesactivarUsuario(int idUsuario, boolean estaActivo, int idUsuarioActor) throws HQLException;

    boolean actualizarUltimoAcceso(int idUsuario, Date fecha) throws HQLException;
}
