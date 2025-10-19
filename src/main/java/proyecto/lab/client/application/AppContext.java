package proyecto.lab.client.application;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.controller.AdminController;

public final class AppContext {
    private static AdminController admin;
    private static UsuarioDTO usuarioActual;

    private AppContext() {
    }

    public static void setAdmin(AdminController c) {
        admin = c;
    }

    public static AdminController admin() {
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
