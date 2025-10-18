package proyecto.lab.server.service;
import proyecto.lab.client.application.App;
import proyecto.lab.server.dto.UsuarioBusquedaDTO;
import proyecto.lab.server.dto.UsuarioUpdateDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.models.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

        final String nombre = user.getNombre().trim();

        try {
            List<Usuario> candidatos = usuariodao.buscarUsuarioPorNombre(nombre);
            boolean existeExacto = candidatos.stream()
                    .anyMatch(u -> nombre.equals(u.getNombre()));
            if (existeExacto)
                throw AppException.conflict("El usuario ya existe en el sistema.");
//            Usuario existente = usuariodao.buscarUsuarioPorID(user.getNombre()); // Buscar al usuario con el nombre  //PARCHE TEMPORAL HASTA QUE EXISTA LA BD
            String hash = BCrypt.hashpw(user.getContrasena(), BCrypt.gensalt(12));
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
    public UsuarioDTO iniciarSesion(UsuarioLoginDTO user) {
        final String nombre = user.getNombre().trim();
        final String contrasena = user.getContrasena();

        try{
            List<Usuario> candidatos = usuariodao.buscarUsuarioPorNombre(nombre);
            Usuario existente = candidatos.stream()
                    .filter(u -> nombre.equals(u.getNombre()))
                    .findFirst()
                    .orElse(null);

            if (existente == null || existente.getContrasena() == null) {
                throw AppException.unauthorized("Credenciales inválidas.");
            }
            if (Objects.equals(existente.getEstado(), "deshabilitado")) {
                throw AppException.forbidden("El usuario está deshabilitado del sistema.");
            }

            String hashedPassword = existente.getContrasena();

            if(BCrypt.checkpw(contrasena, hashedPassword)) {
                return new UsuarioDTO(
                        existente.getID(),
                        existente.getNombre(),
                        existente.getEstado()
                );
            }else{
                throw AppException.unauthorized("Las credenciales ingresadas son incorrectas.");
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al acceder a la base de datos.");
        }

    }

    public UsuarioDTO buscarUsuarioPorId(UsuarioBusquedaDTO in){
        final Integer id = in.getId();
        if(id == null || id <= 0 ){
            throw AppException.badRequest("Debe proporcionar un ID válido. Si no tiene el ID, busque por otro filtro.");
        }
        try{
            Usuario u = usuariodao.buscarUsuarioPorID(id);
            if (u == null) {
                throw AppException.notFound("Usuario no encontrado.");
            }
            return new UsuarioDTO(u.getID(), u.getNombre(), u.getEstado());
        } catch (SQLException e){
            throw AppException.internal("Error al acceder a la base de datos.");
        }

    }

    public List<UsuarioDTO> buscarUsuarios(UsuarioBusquedaDTO in){
        final String nombre = in.getNombre() != null ? in.getNombre().trim() : null;
        final String estado = in.getEstado() != null ? in.getEstado().trim().toLowerCase() : null;

        final boolean hasNombre = nombre != null && !nombre.isEmpty();
        final boolean hasEstado = estado != null && !estado.isEmpty();

        if(!hasNombre && !hasEstado){
            throw AppException.badRequest("Debes ingresar un valor para realizar la búsqueda.");
        }
        try{
            //Ambos filtros presentes - Prioriza nombre
            if(hasNombre &&  hasEstado){
                List<Usuario> lista = usuariodao.buscarUsuarioPorNombre(nombre);
                return lista.stream()
                        .map(u -> new UsuarioDTO(u.getID(),u.getNombre(),u.getEstado()))
                        .toList();
            }
            // Solo nombre
            if(hasNombre){
                List<Usuario> lista = usuariodao.buscarUsuarioPorNombre(nombre);
                return lista.stream()
                        .map(u -> new UsuarioDTO(u.getID(),u.getNombre(),u.getEstado()))
                        .toList();
            }
            // Solo estados con valor habilitado y deshabilitado
            if (!"habilitado".equals(estado) && !"deshabilitado".equals(estado)) {
                throw AppException.badRequest("El estado debe ser 'habilitado' o 'deshabilitado'");
            }

            List<Usuario> lista = usuariodao.buscarUsuarioPorEstado(estado);
            return lista.stream()
                    .map(u -> new UsuarioDTO(u.getID(),u.getNombre(), u.getEstado()))
                    .toList();
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


