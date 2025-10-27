package proyecto.lab.client.application;
import proyecto.lab.server.controller.UsuarioController;
import proyecto.lab.server.dto.UsuarioDTO;

public final class AppContext {
    private static UsuarioController admin;
    private static UsuarioDTO usuarioActual;

    private AppContext() {
    }

    public static void setAdmin(UsuarioController c) {
        admin = c;
    }

    public static UsuarioController admin() {
        return admin;
    }

    public static void setUsuarioActual(UsuarioDTO u) {
        usuarioActual = u; }

    public static UsuarioDTO getUsuarioActual() {
        return usuarioActual;
    }

    public static void LimpiarSesion(){
        usuarioActual = null;
    }

    //public static boolean isAdmin() {
    //    return currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRol());
    //}
}
