package proyecto.lab.server.security;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Rol;

/**
 * Utilidad para manejar autorización según roles.
 * No maneja login ni contraseña, solo permisos.
 */

public final class AuthUtils {

    private AuthUtils() {}

// Si requiere estar logeado
    public static void requireAuthenticated(UsuarioDTO usuario){
        if(usuario == null)
            throw AppException.unauthorized("No autenticado.");
    }
// Verificación si requiere un rol en especifico
    public static void requireRole(UsuarioDTO usuario,Rol... rolesPermitidos){
        requireAuthenticated(usuario);
        Rol actual = usuario.getRol();
        if(actual != null){
            for(Rol r: rolesPermitidos){
                if(actual == r) return;
            }
        }
        throw AppException.forbidden("No tiene permisos para esta acción.");
    }
// Verificación si el usuario tiene un rol
    public static boolean hasRole(UsuarioDTO usuario,Rol rol){
        return usuario != null && usuario.getRol() == rol;
    }


}
