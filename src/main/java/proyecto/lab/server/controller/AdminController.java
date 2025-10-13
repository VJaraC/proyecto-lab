package proyecto.lab.server.controller;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.service.UsuarioService;

import java.util.List;

public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }


    //Casos de uso

    public UsuarioDTO crearUsuario(UsuarioLoginDTO in){
        validarNoNulo(in, "Datos requeridos");
        validarTexto(in.getNombre(), "El nombre es obligatorio");
        validarMinLen(in.getContrasena(), 4, "La contrasena debe tener al menos 4 caracteres");
        return usuarioService.crearUsuario(in);
    }

    private static void validarNoNulo(Object o, String msg){
        if(o == null) throw new IllegalArgumentException(msg);
    }

    private static void validarTexto(String s, String msg){
        if(s == null || s.trim().isEmpty()) throw new IllegalArgumentException(msg);
    }

    private static void validarPositivo(int n, String msg){
        if (n<=0) throw new IllegalArgumentException(msg);
    }
    private static void validarNoNegativo(int n, String msg){
        if (n<0) throw new IllegalArgumentException(msg);
    }
    private static void validarMinLen(String s, int n, String msg){
        if(s == null || s.length() < n) throw new IllegalArgumentException(msg);
    }
    private static void validarRango(int v, int min, int max, String msg){
        if(v<min || v>max) throw new IllegalArgumentException(msg);
    }
}
