package proyecto.lab.server.service;
import proyecto.lab.server.dto.UsuarioBusquedaDTO;
import proyecto.lab.server.dto.UsuarioUpdateDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.models.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import proyecto.lab.server.utils.RutUtils;
import proyecto.lab.server.utils.EstadoUtils;

public class UsuarioService {
    private final UsuarioDAO usuariodao;

    public UsuarioService(UsuarioDAO usuariodao) {
        this.usuariodao = usuariodao;
    }

    public UsuarioDTO crearUsuario(UsuarioLoginDTO user) {
        if (user.getRut() == null || user.getRut().isEmpty()) {
            throw AppException.badRequest("El RUT no puede estar vacío"); // validaciones
        }
        if(user.getRut().length() >  12 ){
            throw AppException.badRequest("El rut debe ser menor o igual a 12 caracteres");
        }
        if(user.getNombre() == null || user.getNombre().trim().isEmpty()) {
            throw AppException.badRequest("El nombre es obligatorio");
        }
        if(user.getNombre().length() > 100) {
            throw AppException.badRequest("El nombre no puede superar 100 caracteres");
        }
        if (user.getContrasena() == null || user.getContrasena().isEmpty() || user.getContrasena().length() < 4) {
            throw AppException.badRequest("La contraseña debe tener al menos 4 caracteres.");// validaciones
        }
        if(user.getContrasena().length() > 255) {
            throw AppException.badRequest("La contraseña es demasiado larga");
        }


        final String rutNormalizado;

        try {
            rutNormalizado= RutUtils.normalizarRut(user.getRut());
            Usuario existente = usuariodao.buscarUsuarioPorRut(rutNormalizado);
            if(existente != null){ throw AppException.badRequest("Ya existe un usuario registrado con ese RUT."); }
            //hashear contraseña
            String hash = BCrypt.hashpw(user.getContrasena(), BCrypt.gensalt(12));

            // crear nuevo usuario
            Usuario nuevo = new Usuario(rutNormalizado ,user.getNombre(), EstadoUtils.HABILITADO, hash);
            Usuario guardado = usuariodao.insertarUsuario(nuevo);

            return new UsuarioDTO(
                    guardado.getID(),
                    guardado.getRut(),
                    guardado.getNombre(),
                    guardado.getEstado()
            );

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw AppException.internal("Error inesperado al crear usuario.");
        }

    }

    public UsuarioDTO iniciarSesion(UsuarioLoginDTO user) {
        if (user.getRut() == null || user.getRut().isEmpty()) {
            throw AppException.badRequest("El RUT es obligatorio.");
        }
        if (user.getContrasena() == null || user.getContrasena().isEmpty()) {
            throw AppException.badRequest("La contraseña es obligatoria.");
        }

        final String rutNormalizado;
        try {
            // Validar y normalizar el RUT al formato estándar (12.345.678-9)
            rutNormalizado = RutUtils.normalizarRut(user.getRut());
        } catch (IllegalArgumentException e) {
            throw AppException.badRequest(e.getMessage());
        }

        final String contrasena = user.getContrasena();

        try {
            // ✅ Buscar usuario directamente por RUT (único)
            Usuario existente = usuariodao.buscarUsuarioPorRut(rutNormalizado);

            if (existente == null || existente.getContrasena() == null) {
                throw AppException.unauthorized("Credenciales inválidas.");
            }

            if (EstadoUtils.DESHABILITADO.equals(existente.getEstado())) {
                throw AppException.forbidden("El usuario está deshabilitado del sistema.");
            }

            //  Verificar contraseña
            String hash = existente.getContrasena();
            if (BCrypt.checkpw(contrasena, hash)) {
                // Login correcto → devolver DTO
                return new UsuarioDTO(
                        existente.getID(),
                        existente.getRut(),
                        existente.getNombre(),
                        existente.getEstado()
                );
            } else {
                throw AppException.unauthorized("Las credenciales ingresadas son incorrectas.");
            }

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw AppException.internal("Error inesperado al iniciar sesión.");
        }
    }


    public UsuarioDTO buscarUsuarioPorId(UsuarioBusquedaDTO in){
        if (in == null) { throw AppException.badRequest("Solicitud inválida.");}
        final Integer id = in.getId();
        if(id == null || id <= 0 ){
            throw AppException.badRequest("Debe proporcionar un ID válido. Si no tiene el ID, busque por otro filtro.");
        }
        try{
            Usuario u = usuariodao.buscarUsuarioPorID(id);
            if (u == null) {
                throw AppException.notFound("Usuario no encontrado.");
            }
            return new UsuarioDTO(u.getID(),u.getRut(), u.getNombre(), u.getEstado());
        } catch (AppException e){
            throw e;
        } catch (Exception e) {
            throw AppException.internal("Error inesperado al buscar por ID.");
        }
    }

    public UsuarioDTO buscarUsuarioPorRut(UsuarioBusquedaDTO in){
        if(in == null || in.getRut() == null){
            throw AppException.badRequest("El RUT es obligatorio.");
        }

        String rutNormalizado;
        try{
            rutNormalizado = RutUtils.normalizarRut(in.getRut());
        }catch(IllegalArgumentException e){
            throw AppException.badRequest(e.getMessage());
        }

        Usuario u = usuariodao.buscarUsuarioPorRut(rutNormalizado);

        if (u == null) {
            throw AppException.notFound("Usuario no encontrado.");
        }

        return new UsuarioDTO(u.getID(), u.getRut(), u.getNombre(), u.getEstado());
    }

    public List<UsuarioDTO> buscarUsuarios(UsuarioBusquedaDTO in){
        if (in == null) { throw AppException.badRequest("Solicitud inválida.");}
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
                        .map(u -> new UsuarioDTO(u.getID(),u.getRut(), u.getNombre(),u.getEstado()))
                        .toList();
            }
            // Solo nombre
            if(hasNombre){
                List<Usuario> lista = usuariodao.buscarUsuarioPorNombre(nombre);
                return lista.stream()
                        .map(u -> new UsuarioDTO(u.getID(), u.getRut(), u.getNombre(),u.getEstado()))
                        .toList();
            }
            // Solo estados con valor habilitado y deshabilitado
            if (!EstadoUtils.esValido(estado)) {
                throw AppException.badRequest("El estado debe ser 'habilitado' o 'deshabilitado'");
            }

            List<Usuario> lista = usuariodao.buscarUsuarioPorEstado(estado);
            return lista.stream()
                    .map(u -> new UsuarioDTO(u.getID(),u.getRut(), u.getNombre(), u.getEstado()))
                    .toList();
        } catch (AppException e) {
            throw e;
        } catch(Exception e) {
            throw AppException.internal("Error inesperado al buscar usuarios.");
        }
    }


    public List<UsuarioDTO> listarUsuarios() {
           try{
               List<Usuario> usuarios = usuariodao.mostrarUsuarios();
               List<UsuarioDTO> listaUsuarios = new ArrayList<>();

               for (Usuario u : usuarios) {
                   listaUsuarios.add(new UsuarioDTO(u.getID(),u.getRut(), u.getNombre(), u.getEstado()));
               }

               return listaUsuarios;
              } catch (AppException e){
                throw e;
           } catch(Exception e) {
               throw AppException.internal("Error inesperado al listar usuarios.");
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
                String nuevoEstado = EstadoUtils.normalizar(dto.getEstado());
                if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                    throw AppException.badRequest("El estado no puede estar vacío");} // Verifica si el estado entregado es vacío

                if (!EstadoUtils.esValido(nuevoEstado)){ // Verifica si el estado entregado es igual al de la BD.
                    throw AppException.badRequest("El estado ya está asignado en el sistema");
                }

                if (nuevoEstado.equalsIgnoreCase(existente.getEstado())){
                    throw AppException.conflict("El estado ya está asignado en el sistema.");
                }
                existente.setEstado(nuevoEstado);
                cambios=true;
            }

            if(!cambios) {
                throw AppException.badRequest("No hay cambios para aplicar");
            }
            boolean ok = usuariodao.actualizarUsuario(existente);
            if(!ok) throw AppException.internal("Error al actualizar usuario");

            return new UsuarioDTO(existente.getID(), existente.getRut(), existente.getNombre(), existente.getEstado());
        } catch (AppException e){
            throw e;
        } catch(Exception e) {
            throw AppException.internal("Error inesperado al actualizar usuarios.");
        }
    }







}


