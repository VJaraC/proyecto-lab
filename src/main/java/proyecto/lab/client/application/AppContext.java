package proyecto.lab.client.application;
import proyecto.lab.server.controller.*;
import proyecto.lab.server.dto.UsuarioDTO;

public final class AppContext {
    private static UsuarioController admin;
    private static UsuarioDTO usuarioActual;
    private static EquipoController equipoController;
    private static LaboratorioController laboratorioController;
    private static SesionController sesionController;
    private static AlertaController alertaController;
    private static MetricasController metricasController;
    private static StatusController statusController;

    private AppContext() {
    }

    public static void setAdmin(UsuarioController c) {
        admin = c;
    }

    public static UsuarioController admin() {
        return admin;
    }

    public static void setEquipoController(EquipoController e) {
        equipoController = e;
    }

    public static EquipoController equipo() {
        return equipoController;
    }

    public static void setLaboratorioController(LaboratorioController c) {
        laboratorioController = c;
    }

    public static LaboratorioController laboratorio() {
        return laboratorioController;
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

    public static void setSesionController(SesionController c) { sesionController = c; }

    public static SesionController sesion() { return sesionController; }

    public static void setAlertaController(AlertaController c) { alertaController = c; }

    public static AlertaController alerta() { return alertaController; }

    public static void setMetricasController(MetricasController c) { metricasController = c; }

    public static MetricasController metricas() { return metricasController; }

    public static void setStatusController(StatusController c) { statusController = c; }

    public static StatusController status() { return statusController; }


}
