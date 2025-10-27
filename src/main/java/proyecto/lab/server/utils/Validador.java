package proyecto.lab.server.utils;

import proyecto.lab.server.exceptions.AppException;

public class Validador {
    private Validador(){}

    public static void validarNoNulo(Object o, String msg){
        if(o == null) throw AppException.badRequest(msg);
    }

    public static void validarEstado(String o, String msg){
        if (!EstadoUtils.esValido(o)) throw AppException.badRequest(msg);
    }
    public static void validarTexto(String s, String msg){
        if(s == null || s.trim().isEmpty()) throw AppException.badRequest(msg);
    }

    public static void validarPositivo(int n, String msg){
        if (n<=0) throw AppException.badRequest(msg);
    }
    public static void validarNoNegativo(int n, String msg){
        if (n<0) throw AppException.badRequest(msg);
    }
    public static void validarMinLen(String s, int n, String msg){
        if(s == null || s.length() < n) throw AppException.badRequest(msg);
    }
    public static void validarRango(int v, int min, int max, String msg){
        if(v<min || v>max) throw AppException.badRequest(msg);
    }
}
