package proyecto.lab.client.application;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import proyecto.lab.server.controller.*;
import proyecto.lab.server.dao.*;
import proyecto.lab.server.service.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.text.Font;
public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        try {
            CloudflaredManager.startTunnel();
        } catch (Exception e) {
            e.printStackTrace();
            // Si falla el túnel, no tiene sentido seguir,
            // porque la app no podrá hablar con la BD
            // Aquí podrías mostrar un Alert más bonito si quieres
            System.err.println("No se pudo iniciar el túnel Cloudflared: " + e.getMessage());
            System.exit(1);
        }

        // --- 1. CARGA DE FUENTES (Solución al problema) ---
        // Cargamos las fuentes en memoria para que el CSS las reconozca.
        // El tamaño '12' no importa, solo queremos registrar la familia.
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/Montserrat-Regular.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Montserrat-Bold.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Product-Sans-Regular.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Product-Sans-Bold.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Core-Mellow-Regular.otf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Core-Mellow-Bold.otf"), 12);
            System.out.println("Fuentes cargadas correctamente en memoria.");
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudieron cargar algunas fuentes. " + e.getMessage());
        }

        //Se inicializan dependencias del backend disponibles globalmente

        // Controladores de Usuario
        UsuarioDAO usuarioDao = new UsuarioDAO();
        UsuarioService usuarioService = new UsuarioService(usuarioDao);
        UsuarioController admin = new UsuarioController(usuarioService);
        AppContext.setAdmin(admin);

        // Controladores de Equipo
        EquipoDAO equipoDao = new EquipoDAO();
        EquipoService equipoService = new EquipoService(equipoDao);
        EquipoController equipoController = new EquipoController(equipoService);
        AppContext.setEquipoController(equipoController);

        // Controladores de Laboratorios
        LaboratorioDAO labDao = new LaboratorioDAO();
        LaboratorioService laboratorioService = new LaboratorioService(labDao);
        LaboratorioController laboratorioController = new LaboratorioController(laboratorioService);
        AppContext.setLaboratorioController(laboratorioController);

        // Controladores de Sesiones

        SesionDAO sesionDao = new SesionDAO();
        SesionService sesionService = new SesionService(sesionDao);
        SesionController sesionController = new SesionController(sesionService);
        AppContext.setSesionController(sesionController);

        // Controladores de Alertas

        AlertaDAO alertaDao = new AlertaDAO();
        AlertaService alertaService = new AlertaService(alertaDao);
        AlertaController alertaController = new AlertaController(alertaService);
        AppContext.setAlertaController(alertaController);

        // Controladores de Metricas

        MetricasDAO metricasDao = new MetricasDAO();
        MetricasService metricasService = new MetricasService(metricasDao);
        MetricasController metricasController = new MetricasController(metricasService);
        AppContext.setMetricasController(metricasController);

        // Controladores StatusController

        ServerStatusDAO serverStatusDao = new ServerStatusDAO();
        StatusService statusService = new StatusService(serverStatusDao);
        StatusController statusController = new StatusController(statusService);
        AppContext.setStatusController (statusController);

        // Se lanza la vista
        Parent root = FXMLLoader.load(getClass().getResource("/views/IniciarSesion.fxml"));
        stage.setTitle("Sistema de Monitoreo - UNAP");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // cerrar túnel al cerrar la app
        CloudflaredManager.stopTunnel();
        super.stop();
    }

    private void mostrarErrorFatal(String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de conexión");
            alert.setHeaderText("No se pudo iniciar la aplicación");
            alert.setContentText(mensaje);
            alert.showAndWait();
            Platform.exit();
        });
    }
    public static void main(String[] args) {
        launch(args);
    }

}