package proyecto.lab.server.service;
import proyecto.lab.server.dto.UsuarioUpdateDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.models.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public List<UsuarioDTO> listarUsuarios() {
           try{
               List<Usuario> usuarios = usuariodao.mostrarUsuarios();
               List<UsuarioDTO> listaUsuarios = new ArrayList<>();

               for (Usuario u : usuarios) {
                   listaUsuarios.add(new UsuarioDTO(u.getID(), u.getNombre(), u.getEstado()));
               }

               return listaUsuarios;
              } catch (SQLException e){
                throw AppException.internal("Error al listar usuarios" + e.getMessage());
           }
    }





    public UsuarioDTO actualizarUsuario(UsuarioUpdateDTO dto) {
        try {
            Usuario existente = usuariodao.buscarUsuarioPorID(dto.getId()); // Verificar si el usuario a cambiar existe
            if (existente == null) {
                throw AppException.notFound("Usuario no encontrado");
            }

            boolean cambios = false;

            if (dto.getNombre() != null) { // Si hubo un cambio en el nombre, se actualiza
                String nuevoNombre = dto.getNombre().trim();
                if (nuevoNombre.isEmpty()) throw AppException.badRequest("El nombre no puede estar vacío"); // Verifica si el nombre entregado es vacío

                if (nuevoNombre.equals(existente.getNombre())){  // Verifica si el nombre entregado es igual al de la BD.
                    throw AppException.conflict("El nombre existe en el sistema.");
                }
                existente.setNombre(nuevoNombre);
                cambios = true;
            }
            if (dto.getEstado() != null){ // Si hubo cambio en el estado, se actualiza
                String nuevoEstado = dto.getEstado().trim();
                if (nuevoEstado.isEmpty()) {throw AppException.badRequest("El estado no puede estar vacío");} // Verifica si el estado entregado es vacío

                if (nuevoEstado.equals(existente.getEstado())){ // Verifica si el estado entregado es igual al de la BD.
                    throw AppException.conflict("El estado ya está asignado en el sistema");
                }

                existente.setEstado(nuevoEstado);
                cambios=true;
            }

            if(!cambios) {
                throw AppException.badRequest("No hay cambios para aplicar");
            }
            boolean ok = usuariodao.actualizarUsuario(existente);
            if(!ok) throw AppException.internal("Error al actualizar usuario");

            return new UsuarioDTO(existente.getID(),existente.getNombre(), existente.getEstado());
        } catch (SQLException e){
            throw AppException.internal("Error al actualizar usuario: " + e.getMessage());
        }
    }







}


