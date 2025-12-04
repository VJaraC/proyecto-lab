package proyecto.lab.client.application;
import javafx.application.Application;
import javafx.stage.Stage;
import proyecto.lab.server.controller.*;
import proyecto.lab.server.dao.*;
import proyecto.lab.server.service.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {

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

        // Se lanza la vista
        Parent root = FXMLLoader.load(getClass().getResource("/views/IniciarSesion.fxml"));
        stage.setTitle("Sistema de Monitoreo - UNAP");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}