package proyecto.lab.client.application;
import proyecto.lab.server.controller.EquipoController;
import proyecto.lab.server.controller.UsuarioController;
import proyecto.lab.server.dto.UsuarioDTO;

public final class AppContext {
    private static UsuarioController admin;
    private static UsuarioDTO usuarioActual;
    private static EquipoController equipoController;

    private AppContext() {
    }

    public static void setAdmin(UsuarioController c) {
        admin = c;
    }

    public static UsuarioController admin() {
        return admin;
    }

    public static void setUsuarioActual(UsuarioDTO u) {
        usuarioActual = u;
    }

    public static UsuarioDTO getUsuarioActual() {
        return usuarioActual;
    }

    public static void LimpiarSesion() {
        usuarioActual = null;
    }

    public static void setEquipoController(EquipoController e) {
        equipoController = e;
    }

    public static EquipoController equipo() {
        return equipoController;
    }
}
