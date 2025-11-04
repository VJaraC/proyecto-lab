package proyecto.lab.server.controller;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.dto.UsuarioUpdateDTO;
import proyecto.lab.server.dto.UsuarioBusquedaDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.security.AuthUtils;
import proyecto.lab.server.service.UsuarioService;
import proyecto.lab.server.utils.RutUtils;
import proyecto.lab.server.utils.EstadoUtils;
import java.util.List;
import static proyecto.lab.server.utils.ValidadorUtils.*;

public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    //Casos de uso

   // ====================== Autenticación ======================
    // No requiere rol, es login
    public UsuarioDTO iniciarSesion(UsuarioLoginDTO in){
        validarNoNulo(in, "Datos requeridos");
        validarTexto(in.getRut(), "El RUT es obligatorio");
        validarTexto(in.getContrasena(), "La contraseña es obligatoria");
        return usuarioService.iniciarSesion(in);
    }

    // =================== Comandos (ADMIN) ===================

    public UsuarioDTO crearUsuario(UsuarioLoginDTO in, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        validarNoNulo(in, "Datos requeridos");
        validarTexto(in.getRut(), "El RUT es obligatorio");
        validarTexto(in.getNombre(), "El nombre es obligatorio");
        validarTexto(in.getApellidos() , "El apellidos es obligatorio");
        validarTexto(in.getEmail(), "El email es obligatorio");
        validarTexto(in.getGenero(), "El genero es obligatorio");
        validarMinLen(in.getContrasena(), 4, "La contraseña debe tener al menos 4 caracteres");
        validarTexto(in.getCargo(), "El cargo es obligatorio");
        validarTexto(in.getTelefono(), "El telefono es obligatorio");
        return usuarioService.crearUsuario(in);
    }

    public UsuarioDTO modificarNombreUsuario(UsuarioUpdateDTO in , String nuevonombre, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        int id = in.id();
        validarNoNulo(in, "Datos requeridos");
        validarTexto(nuevonombre, "Debes ingresar un nombre");
        validarPositivo(id, "El id es obligatorio");

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(id, nuevonombre, null, null, null, null, null, null);

        return usuarioService.actualizarUsuario(dto);
    }
    public UsuarioDTO modificarApellidoUsuario(UsuarioUpdateDTO in , String nuevoapellidos, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        int id = in.id();
        validarPositivo(id, "El id es obligatorio");
        validarNoNulo(in, "Datos requeridos");
        validarTexto(nuevoapellidos, "Debes ingresar apellidos.");

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(id, null, nuevoapellidos, null, null, null, null, null);

        return usuarioService.actualizarUsuario(dto);
    }

    public UsuarioDTO actualizarRolUsuario(UsuarioUpdateDTO in,Rol nuevoRol, UsuarioDTO auth ){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        int id = in.id();

        validarNoNulo(in, "Datos requeridos");
        validarPositivo(in.id(), "ID inválido");
        if (nuevoRol== null) throw AppException.badRequest("Debes seleccionar un rol válido.");

        if (auth.getID() == id){
            throw AppException.forbidden("No puedes cambiar tu propio rol.");
        }

        UsuarioUpdateDTO dto= new UsuarioUpdateDTO(
                in.id(),
                null,
                null,
                null,
                null,
                null,
                null,
                nuevoRol
        );
        return usuarioService.actualizarUsuario(dto);
    }

    public UsuarioDTO modificarCorreoUsuario(UsuarioUpdateDTO in , String nuevocorreo, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        int id = in.id();
        validarPositivo(id, "El id es obligatorio");
        validarNoNulo(in, "Datos requeridos");
        validarTexto(nuevocorreo, "Debes ingresar un correo válido.");

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(id, null, null, null, nuevocorreo, null, null, null);

        return usuarioService.actualizarUsuario(dto);
    }

    public UsuarioDTO modificarTelefonoUsuario(UsuarioUpdateDTO in , String nuevotelefono, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        int id = in.id();
        validarPositivo(id, "El id es obligatorio");
        validarNoNulo(in, "Datos requeridos");
        validarTexto(nuevotelefono, "Debes ingresar un teléfono válido.");

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(id, null, null, null, null, nuevotelefono, null, null);

        return usuarioService.actualizarUsuario(dto);
    }

    public UsuarioDTO modificarCargoUsuario(UsuarioUpdateDTO in , String nuevocargo, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        int id = in.id();
        validarPositivo(id, "El id es obligatorio");
        validarNoNulo(in, "Datos requeridos");
        validarTexto(nuevocargo, "Debes ingresar un cargo válido.");

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(id, null, null, null, null, null, nuevocargo, null);

        return usuarioService.actualizarUsuario(dto);
    }

    public UsuarioDTO habilitarUsuario(UsuarioUpdateDTO in, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        int id = in.id();
        validarPositivo(id, "El id es obligatorio");
        validarNoNulo(in, "Datos requeridos");
        validarPositivo(in.id(),"ID invalido");

        String estado = EstadoUtils.HABILITADO;

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(id, null, null, estado, null, null, null, null);
        return usuarioService.actualizarUsuario(dto);
    }

    public UsuarioDTO deshabilitarUsuario(UsuarioUpdateDTO in, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        int id = in.id();
        validarNoNulo(in, "Datos requeridos");
        validarPositivo(id,"ID invalido");

        String estado = EstadoUtils.DESHABILITADO;

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(id, null, null, estado, null, null, null, null);
        return usuarioService.actualizarUsuario(dto);
    }

    public UsuarioDTO buscarUsuarioPorRUT(String rut, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        validarNoNulo(rut, "El RUT es obligatorio");

        // Normalizar + Validar DV
        final String rutnormalizado;
        try{
            rutnormalizado = RutUtils.normalizarRut(rut);
        }catch(IllegalArgumentException e){
            throw AppException.badRequest(e.getMessage());
        }

        UsuarioBusquedaDTO busqueda = new UsuarioBusquedaDTO();
        busqueda.setRut(rutnormalizado);

        return usuarioService.buscarUsuarioPorRut(busqueda);
    }

    public UsuarioDTO buscarUsuarioPorId(Integer id, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        validarNoNulo(id, "El id es obligatorio");
        validarPositivo(id, "El id debe ser positivo");

        UsuarioBusquedaDTO busqueda = new UsuarioBusquedaDTO();
        busqueda.setId(id);
        return usuarioService.buscarUsuarioPorId(busqueda);
    }

    public List<UsuarioDTO> buscarUsuarioPorNombre(String nombre, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        validarTexto(nombre, "El nombre es obligatorio");
        UsuarioBusquedaDTO busqueda = new UsuarioBusquedaDTO();
        busqueda.setNombre(nombre);
        return usuarioService.buscarUsuarios(busqueda);
    }

    public List<UsuarioDTO> buscarUsuarioPorEstado(String estado, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        validarNoNulo(estado, "El estado es obligatorio");
        validarEstado(estado, "El estado debe ser habilitado o deshabilitado");
        UsuarioBusquedaDTO busqueda = new UsuarioBusquedaDTO();
        busqueda.setEstado(estado);
        return usuarioService.buscarUsuarios(busqueda);
    }

    public List<UsuarioDTO> listarUsuarios(UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        return usuarioService.listarUsuarios();
    }

}
