package proyecto.lab.server.service;
import proyecto.lab.server.dto.UsuarioBusquedaDTO;
import proyecto.lab.server.dto.UsuarioUpdateDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.models.Usuario;
import java.time.LocalDate;
import java.time.Period;
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
        LocalDate hoy = LocalDate.now();
        LocalDate fechaNacimiento = user.getFecha_nacimiento();

        if(fechaNacimiento == null) {
            throw AppException.badRequest("La fecha de nacimiento es obligatoria");
        }

        if (fechaNacimiento.isAfter(hoy)) {
            throw AppException.badRequest("La fecha de nacimiento no puede ser posterior a hoy");
        }

        int edad = Period.between(fechaNacimiento,hoy).getYears();
        if (edad < 18){
            throw AppException.badRequest("El usuario debe tener al menos 18 años");
        }

        final String rutNormalizado;

        try {
            rutNormalizado= RutUtils.normalizarRut(user.getRut());

            if(existsByRut(rutNormalizado)){
                throw AppException.conflict("Ya existe un usuario registrado con ese RUT.");
            }

            //hashear contraseña
            String hash = BCrypt.hashpw(user.getContrasena(), BCrypt.gensalt(12));

            // crear nuevo usuario
            Usuario nuevo = new Usuario(rutNormalizado,
                    user.getNombre(),
                    user.getApellidos(),
                    user.getEmail(),
                    EstadoUtils.HABILITADO,
                    user.getGenero(),
                    hash,
                    user.getCargo(),
                    Rol.MONITOR,
                    user.getFecha_nacimiento(),
                    user.getTelefono()
            );
            Usuario guardado = usuariodao.insertarUsuario(nuevo);

            return toDTO(guardado);
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
        try {
            Usuario existente = requireByRut(user.getRut());

            if(EstadoUtils.DESHABILITADO.equals(existente.getEstado())){
                throw AppException.forbidden("El usuario está deshabilitado del sistema.");
            }
            if(BCrypt.checkpw(user.getContrasena(), existente.getContrasena())){
                return toDTO(existente);
            }
            throw AppException.unauthorized("Las credenciales ingresadas son incorrectas.");
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw AppException.internal("Error inesperado al iniciar sesión.");
        }

    }

    public UsuarioDTO buscarUsuarioPorId(UsuarioBusquedaDTO in){
        if (in == null ) throw AppException.badRequest("Solicitud inválida.");
        Usuario u = requireById(in.getId());
        return toDTO(u);
    }

    public UsuarioDTO buscarUsuarioPorRut(UsuarioBusquedaDTO in){
        if (in == null) throw AppException.badRequest("Solicitud inválida.");
        Usuario u = requireByRut(in.getRut());
        return toDTO(u);
    }

    public List<UsuarioDTO> buscarUsuarios(UsuarioBusquedaDTO in) {
        if (in == null) throw AppException.badRequest("Solicitud inválida.");

        boolean tieneFiltro =
                (in.getNombre() != null && !in.getNombre().isBlank()) ||
                        (in.getApellidos() != null && !in.getApellidos().isBlank()) ||
                        (in.getEstado() != null && !in.getEstado().isBlank()) ||
                        (in.getGenero() != null && !in.getGenero().isBlank()) ||
                        (in.getCargo() != null && !in.getCargo().isBlank()) ||
                        (in.getTelefono() != null && !in.getTelefono().isBlank()) ||
                        (in.getFecha_nac() != null);

        if (!tieneFiltro)
            throw AppException.badRequest("Debes especificar al menos un filtro.");

        List<Usuario> usuarios = usuariodao.buscarUsuarios(
                in.getNombre(),
                in.getApellidos(),
                in.getEstado(),
                in.getGenero(),
                in.getCargo(),
                in.getFecha_nac(),
                in.getTelefono()
        );

        if (usuarios.isEmpty()) {
            throw AppException.notFound("No se encontraron usuarios con los filtros indicados.");
        }

        return toDTOs(usuarios);
    }



    public List<UsuarioDTO> listarUsuarios() {
           try{
               List<Usuario> usuarios = usuariodao.mostrarUsuarios();
               List<UsuarioDTO> listaUsuarios = new ArrayList<>();

               for (Usuario u : usuarios) {
                   listaUsuarios.add(toDTO(u));
               }
               return listaUsuarios;
              } catch (AppException e){
                throw e;
           } catch(Exception e) {
               throw AppException.internal("Error inesperado al listar usuarios.");
           }
    }

    public UsuarioDTO actualizarUsuario(UsuarioUpdateDTO dto) {
        try{
            Usuario existente = requireById(dto.id());

            boolean cambios = false;

            if(applyTextChange(dto.nombres(), existente.getNombres(), existente::setNombres, "nombre", true ,true)) cambios = true;
            if(applyTextChange(dto.apellidos(), existente.getApellidos(), existente::setApellidos, "apellido", true ,true)) cambios = true;
            if(applyEstadoChange(dto.estado(),existente)) cambios = true;
            if(applyRolChange(dto.rol(),existente)) cambios = true;
            if(applyTextChange(dto.email(), existente.getEmail(), existente::setEmail, "email", true ,true)) cambios = true;
            if(applyTextChange(dto.telefono(),existente.getTelefono(), existente::setTelefono, "teléfono", true ,true)) cambios = true;
            if(applyTextChange(dto.cargo(),existente.getCargo(), existente::setCargo, "cargo", true ,true)) cambios = true;

            if(!cambios){
                throw AppException.badRequest("No hay cambios para aplicar");
            }

            boolean ok = usuariodao.actualizarUsuario(existente);
            if(!ok){ throw AppException.internal("Error inesperado al actualizar usuario.");}

            return toDTO(existente);
        } catch (AppException e) {
            throw e;
        } catch(Exception e) {
            throw AppException.internal("Error inesperado al actualizar usuario.");
        }
    }

    private UsuarioDTO toDTO(Usuario u){
        return new UsuarioDTO(
                u.getID(),
                u.getRut(),
                u.getNombres(),
                u.getApellidos(),
                u.getEmail(),
                u.getEstado(),
                u.getGenero(),
                u.getCargo(),
                u.getRol(),
                u.getFecha_nac(),
                u.getTelefono()
        );
    }

    private List<UsuarioDTO> toDTOs(List<Usuario> usuarios){
        return usuarios.stream().map(this::toDTO).toList();
    }

    private Usuario requireByRut(String rut){
        if (rut == null || rut.isBlank()){
            throw AppException.badRequest("El rut no puede ser nulo");
        }
        final String normalizado;
        try {
            normalizado = RutUtils.normalizarRut(rut);
        } catch (IllegalArgumentException e){
            throw AppException.badRequest(e.getMessage());
        }

        return usuariodao.buscarUsuarioPorRut(normalizado)
                .orElseThrow(() -> AppException.notFound("Usuario no encontrado"));
    }


    private boolean existsByRut(String rut){
        String normalizado = RutUtils.normalizarRut(rut);
        return usuariodao.existeUsuarioPorRut(normalizado);
    }


    private Usuario requireById(Integer id) {
        if (id == null || id <= 0) {
            throw AppException.badRequest("Debe proporcionar un ID válido.");
        }
        Usuario u = usuariodao.buscarUsuarioPorID(id);
        if (u == null) throw AppException.notFound("Usuario no encontrado.");
        return u;
    }

    private boolean applyTextChange(String nuevo, String actual, java.util.function.Consumer<String> setter,
                                    String etiquetaCampo, boolean rejectEmpty, boolean conflictIfSame) {
        if (nuevo == null) return false;
        String v = nuevo.trim();

        if (rejectEmpty && v.isEmpty()) {
            throw AppException.badRequest("El " + etiquetaCampo + " no puede estar vacío");
        }
        if (conflictIfSame && actual != null && v.equals(actual.trim())) {
            throw AppException.conflict("El " + etiquetaCampo + " ya está asignado en el sistema.");
        }
        setter.accept(v);
        return true;
    }

    private boolean applyEstadoChange(String estadoDto, Usuario existente) {
        if (estadoDto == null) return false;

        String nuevoEstado = EstadoUtils.normalizar(estadoDto);
        if (nuevoEstado.isBlank()) {
            throw AppException.badRequest("El estado no puede estar vacío");
        }
        if (!EstadoUtils.esValido(nuevoEstado)) {
            throw AppException.badRequest("El estado debe ser 'habilitado' o 'deshabilitado'");
        }
        if (nuevoEstado.equalsIgnoreCase(existente.getEstado())) {
            throw AppException.conflict("El estado ya está asignado en el sistema.");
        }
        existente.setEstado(nuevoEstado);
        return true;
    }

    private boolean applyRolChange(Rol rolDto, Usuario existente) {
        if (rolDto == null) return false;
        if (rolDto == existente.getRol()) {
            throw AppException.conflict("El rol ya está asignado en el sistema.");
        }
        existente.setRol(rolDto);
        return true;
    }



}


