package proyecto.lab.server.service;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.models.Usuario;
import java.sql.SQLException;
import java.util.Objects;

public class UsuarioService {
    private final UsuarioDAO usuariodao;

    public UsuarioService(){
        this.usuariodao = new UsuarioDAO();
    }

    public boolean crearUsuario(Usuario user) throws SQLException{
        if(user.getNombre()==null || user.getNombre().isEmpty()){
            throw new SQLException("El nombre no puede estar vacío."); // validaciones
        }
        if(user.getPassword()==null || user.getPassword().isEmpty() || user.getPassword().length() < 4){
            throw new SQLException("La contraseña debe tener al menos 4 caracteres."); // validaciones
        }

        Usuario existente = usuariodao.buscarUsuarioPorID(user.getID()); // Buscar al usuario con el nombre

        if(existente != null){
            throw new SQLException("El usuario ya existe en el sistema."); // Es una validación de ejemplo, esto ya sería por ID, etc.
        }
        usuariodao.insertarUsuario(user);
        return true;
    }

    public boolean actualizarNombreUsuario(Usuario user, String nombre) throws SQLException{
        if(nombre == null || nombre.isEmpty()){
            throw new SQLException("El valor del nombre no puede estar vacio.");
        }

        Usuario existente= usuariodao.buscarUsuarioPorID(user.getID());
        if(existente == null){
            throw new SQLException("El usuario no existe en el sistema.");
        }

        return usuariodao.actualizarNombre(user, nombre);
    }

    public boolean deshabilitarUsuario(Usuario user) throws SQLException {
        return usuariodao.cambiarEstadoUsuario(user, "deshabilitado");
    }

    public boolean habilitarUsuario(Usuario user) throws SQLException {
        return usuariodao.cambiarEstadoUsuario(user, "habilitado");
    }







}


