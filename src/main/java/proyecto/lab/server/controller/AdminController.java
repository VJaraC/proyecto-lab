package proyecto.lab.server.controller;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.dto.UsuarioUpdateDTO;
import proyecto.lab.server.dto.UsuarioBusquedaDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.service.UsuarioService;
import proyecto.lab.server.utils.RutUtils;
import proyecto.lab.server.utils.EstadoUtils;

import java.util.List;
import java.util.Objects;

public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }


    //Casos de uso

    public UsuarioDTO crearUsuario(UsuarioLoginDTO in){
        validarNoNulo(in, "Datos requeridos");
        validarTexto(in.getRut(), "El RUT es obligatorio");
        validarMinLen(in.getContrasena(), 4, "La contraseña debe tener al menos 4 caracteres");
        return usuarioService.crearUsuario(in);
    }

    public UsuarioDTO iniciarSesion(UsuarioLoginDTO in){
        validarNoNulo(in, "Datos requeridos");
        validarTexto(in.getRut(), "El RUT es obligatorio");
        validarTexto(in.getContrasena(), "La contraseña es obligatoria");
        return usuarioService.iniciarSesion(in);
    }

    public UsuarioDTO buscarUsuarioPorRUT(String rut){
        validarNoNulo(rut, "El nombre es obligatorio");

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

    public UsuarioDTO buscarUsuarioPorId(Integer id){
        validarNoNulo(id, "El id es obligatorio");
        validarPositivo(id, "El id debe ser positivo");

        UsuarioBusquedaDTO busqueda = new UsuarioBusquedaDTO();
        busqueda.setId(id);
        return usuarioService.buscarUsuarioPorId(busqueda);
    }

    public List<UsuarioDTO> buscarUsuarioPorNombre(String nombre){
        validarTexto(nombre, "El nombre es obligatorio");
        UsuarioBusquedaDTO busqueda = new UsuarioBusquedaDTO();
        busqueda.setNombre(nombre);
        return usuarioService.buscarUsuarios(busqueda);
    }

    public List<UsuarioDTO> buscarUsuarioPorEstado(String estado){
        validarNoNulo(estado, "El estado es obligatorio");
        validarEstado(estado, "El estado debe ser habilitado o deshabilitado");
        UsuarioBusquedaDTO busqueda = new UsuarioBusquedaDTO();
        busqueda.setEstado(estado);
        return usuarioService.buscarUsuarios(busqueda);
    }

    public UsuarioDTO modificarNombreUsuario(UsuarioUpdateDTO in , String nuevonombre){
        validarNoNulo(in, "Datos requeridos");
        validarTexto(nuevonombre, "Debes ingresar un nombre");

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setId(in.getId());
        dto.setNombre(nuevonombre);

        return usuarioService.actualizarUsuario(dto);
    }

    public UsuarioDTO habilitarUsuario(UsuarioUpdateDTO in){
        validarNoNulo(in, "Datos requeridos");
        validarPositivo(in.getId(),"ID invalido");

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setId(in.getId());
        dto.setEstado(EstadoUtils.HABILITADO);
        return usuarioService.actualizarUsuario(dto);
    }

    public List<UsuarioDTO> listarUsuarios(){
        return usuarioService.listarUsuarios();
    }

    public UsuarioDTO deshabilitarUsuario(UsuarioUpdateDTO in){
        validarNoNulo(in, "Datos requeridos");
        validarPositivo(in.getId(),"ID invalido");

        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setId(in.getId());
        dto.setEstado(EstadoUtils.DESHABILITADO);
        return usuarioService.actualizarUsuario(dto);
    }

    private static void validarNoNulo(Object o, String msg){
        if(o == null) throw AppException.badRequest(msg);
    }

    private static void validarEstado(String o, String msg){
        if (!EstadoUtils.esValido(o)) throw AppException.badRequest(msg);
    }
    private static void validarTexto(String s, String msg){
        if(s == null || s.trim().isEmpty()) throw AppException.badRequest(msg);
    }

    private static void validarPositivo(int n, String msg){
        if (n<=0) throw AppException.badRequest(msg);
    }
    private static void validarNoNegativo(int n, String msg){
        if (n<0) throw AppException.badRequest(msg);
    }
    private static void validarMinLen(String s, int n, String msg){
        if(s == null || s.length() < n) throw AppException.badRequest(msg);
    }
    private static void validarRango(int v, int min, int max, String msg){
        if(v<min || v>max) throw AppException.badRequest(msg);
    }
}
