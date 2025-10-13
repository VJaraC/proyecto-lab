package proyecto.lab.server.service;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.models.Usuario;
import java.sql.SQLException;
import java.util.Objects;

import org.mindrot.jbcrypt.BCrypt;

public class UsuarioService {
    private final UsuarioDAO usuariodao;

    public UsuarioService(UsuarioDAO usuariodao) {
        this.usuariodao = usuariodao;
    }

    public UsuarioDTO crearUsuario(UsuarioLoginDTO user) {
        if (user.getNombre() == null || user.getNombre().isEmpty()) {
            throw AppException.badRequest("El nombre no puede estar vacío"); // validaciones
        }
        if (user.getContrasena() == null || user.getContrasena().isEmpty() || user.getContrasena().length() < 4) {
            throw AppException.badRequest("La contraseña debe tener al menos 4 caracteres.");// validaciones
        }

        try {
            Usuario existente = usuariodao.buscarUsuarioPorNombre(user.getNombre()); // Buscar al usuario con el nombre

            if (existente != null) {
                throw AppException.conflict("El usuario ya existe en el sistema.");// Es una validación de ejemplo, esto ya sería por ID, etc.
            }

            String hash = BCrypt.hashpw(user.getContrasena(), BCrypt.gensalt());
            Usuario nuevo = new Usuario(user.getNombre(), "habilitado", hash);
            Usuario guardado = usuariodao.insertarUsuario(nuevo);

            return new UsuarioDTO(
                    guardado.getID(),
                    guardado.getNombre(),
                    guardado.getEstado()
            );

        } catch (SQLException e) {
            throw AppException.internal("Error al acceder a la base de datos.");
        }

    }

    public boolean actualizarNombreUsuario(Usuario user, String nombre) throws SQLException{
        if(nombre == null || nombre.isEmpty()){
            throw new SQLException("El valor del nombre no puede estar vacio.");
        }
        if(Objects.equals(user.getNombre(), nombre)){
            throw new SQLException("El usuario ya posee este nombre en el sistema");
        }

        Usuario existente= usuariodao.buscarUsuarioPorID(user.getID());
        if(existente == null){
            throw new SQLException("El usuario no existe en el sistema.");
        }

        return usuariodao.actualizarNombre(user, nombre);
    }

    public boolean deshabilitarUsuario(Usuario user) throws SQLException {
        if(Objects.equals(user.getEstado(), "deshabilitado")){ throw new SQLException("El usuario ya está deshabilitado");}
        return usuariodao.cambiarEstadoUsuario(user, "deshabilitado");
    }

    public boolean habilitarUsuario(Usuario user) throws SQLException {
        if(Objects.equals(user.getEstado(), "habilitado")){throw new SQLException("El usuario ya está habilitado");}
        return usuariodao.cambiarEstadoUsuario(user, "habilitado");
    }







}


