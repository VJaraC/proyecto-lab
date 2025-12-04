package proyecto.lab.client.controllers;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.AlertaDetalleDTO;
import proyecto.lab.server.dto.SesionHoraDTO;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewInicioController {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private ProgressIndicator indicadorCargando;

    @FXML
    private AnchorPane panelCarga;

    @FXML
    private AnchorPane panelContenido;

    @FXML
    private StackPane panelRaiz;

    @FXML
    private Label txtUsuarioSesion;

    @FXML
    private CategoryAxis ejeX;

    @FXML
    private NumberAxis ejeY;

    @FXML
    private LineChart<String, Number> graficoSesiones;

    @FXML
    private Label txtEquiposActivos;

    @FXML
    private Label txtEquiposTotales;

    @FXML
    private Label txtEstadoServidor;

    @FXML
    private Label txtFecha;

    @FXML
    private Label txtLabsActivos;

    @FXML
    private Label txtLabsTotales;

    @FXML
    private Label txtSesionesActivas;

    @FXML
    private Label txtUltimaAlerta;

    @FXML
    private Label txtAlertas;

    @FXML
    private AnchorPane panel1;

    @FXML
    private AnchorPane panel2;

    @FXML
    private AnchorPane panel3;

    @FXML
    private AnchorPane panel4;

    @FXML
    private AnchorPane panel5;

    @FXML
    private AnchorPane panel6;

    // Timeline para refresco automático
    private Timeline autoRefresco;

    // Control loader
    private boolean primeraCarga = true;

    // DTO interno para agrupar todos los datos del dashboard
    private static class DashboardData {
        int equiposActivos;
        int equiposTotales;
        int labsActivos;
        int labsTotales;
        int sesionesActivas;
        int alertasRecientes;
        AlertaDetalleDTO ultimaAlertaDetallada;
        List<SesionHoraDTO> sesionesPorHora;
    }

    // ======================================================
    //     EVENTO BOTÓN CERRAR SESIÓN
    // ======================================================

    @FXML
    void btnCerrarSesion(ActionEvent event) {
        if (autoRefresco != null) {
            autoRefresco.stop();
        }

        AppContext.LimpiarSesion();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/IniciarSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Monitoreo - UNAP");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    //     INITIALIZE
    // ======================================================

    @FXML
    void initialize() {

        indicadorCargando.setProgress(-1);

        txtUsuarioSesion.setText(AppContext.getUsuarioActual().getNombres());
        txtFecha.setText(LocalDate.now().toString());

        configurarGraficoBase();
        setearDashboard();

        animacionTraslacion(panel1);
        animacionTraslacion(panel2);
        animacionTraslacion(panel3);
        animacionTraslacion(panel4);
        animacionTraslacion(panel5);
        animacionTraslacion(panel6);

        iniciarRefrescoAutomatico();
    }

    // ======================================================
    //     DASHBOARD LÓGICA
    // ======================================================

    public void setearDashboard() {
        refrescarEnBackground();
    }

    private void refrescarEnBackground() {

        // Loader solo primera carga
        if (primeraCarga) {
            Platform.runLater(() -> mostrarLoading(true));
        }

        Task<DashboardData> task = new Task<>() {
            @Override
            protected DashboardData call() {
                return cargarDatosDashboard();
            }
        };

        task.setOnSucceeded(event -> {
            DashboardData data = task.getValue();
            if (data != null) {
                actualizarDashboardUI(data);
            }

            if (primeraCarga) {
                primeraCarga = false;
                Platform.runLater(() -> mostrarLoading(false));
            }
        });

        task.setOnFailed(event -> {
            task.getException().printStackTrace();

            if (primeraCarga) {
                primeraCarga = false;
                Platform.runLater(() -> mostrarLoading(false));
            }
        });

        Thread t = new Thread(task, "Dashboard-Refresco");
        t.setDaemon(true);
        t.start();
    }

    private DashboardData cargarDatosDashboard() {
        DashboardData data = new DashboardData();

        data.equiposActivos = AppContext.equipo().contarEquipoSesionActiva();
        data.equiposTotales = AppContext.equipo().contarEquiposActivos();

        data.labsActivos = AppContext.laboratorio().contarLaboratoriosSesionActiva();
        data.labsTotales = AppContext.laboratorio().contarLaboratoriosActivos();

        data.sesionesActivas = AppContext.sesion().contarSesionesActivas();

        data.alertasRecientes = AppContext.alerta().contarAlertasRecientes();
        data.ultimaAlertaDetallada = AppContext.alerta().obtenerUltimaAlerta();

        data.sesionesPorHora = AppContext.sesion().obtenerSesionesPorHora();

        return data;
    }

    private void actualizarDashboardUI(DashboardData data) {

        txtEquiposActivos.setText(String.valueOf(data.equiposActivos));
        txtEquiposTotales.setText(String.valueOf(data.equiposTotales));

        txtLabsActivos.setText(String.valueOf(data.labsActivos));
        txtLabsTotales.setText(String.valueOf(data.labsTotales));

        txtSesionesActivas.setText(String.valueOf(data.sesionesActivas));

        txtAlertas.setText(String.valueOf(data.alertasRecientes));

        if (data.ultimaAlertaDetallada != null) {
            Timestamp ts = data.ultimaAlertaDetallada.getFechaAlerta();
            ZonedDateTime fechaChile = ts.toInstant().atZone(ZoneId.of("America/Santiago"));
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String horaFormateada = fechaChile.format(fmt);

            String textoAlerta = "HORA: " + horaFormateada +
                    "\nEQUIPO: " + data.ultimaAlertaDetallada.getHostname() +
                    ". ALERTA DE " + data.ultimaAlertaDetallada.getNombreMetrica() +
                    " : " + data.ultimaAlertaDetallada.getMensaje();

            txtUltimaAlerta.setText(textoAlerta);
        } else {
            txtUltimaAlerta.setText("Sin alertas registradas.");
        }

        configurarGraficoSesiones(data.sesionesPorHora);
    }

    // ======================================================
    //     GRÁFICO
    // ======================================================

    private void configurarGraficoBase() {
        ejeX.setLabel("Hora");
        ejeY.setLabel("Sesiones activas");
        graficoSesiones.setAnimated(false);
        graficoSesiones.setLegendVisible(false);
    }

    private void configurarGraficoSesiones(List<SesionHoraDTO> reporte) {
        if (reporte == null) return;

        XYChart.Series<String, Number> series;

        if (graficoSesiones.getData().isEmpty()) {
            series = new XYChart.Series<>();
            series.setName("Sesiones hoy");
            graficoSesiones.getData().add(series);
        } else {
            series = graficoSesiones.getData().get(0);
        }

        series.getData().clear();

        for (SesionHoraDTO dto : reporte) {
            series.getData().add(new XYChart.Data<>(dto.hora(), dto.cantidad()));
        }
    }

    // ======================================================
    //     REFRESCO AUTOMÁTICO
    // ======================================================

    private void iniciarRefrescoAutomatico() {
        autoRefresco = new Timeline(
                new KeyFrame(
                        Duration.seconds(10),
                        event -> refrescarEnBackground()
                )
        );
        autoRefresco.setCycleCount(Animation.INDEFINITE);
        autoRefresco.play();
    }

    // ======================================================
    //     LOADING ANIMATION
    // ======================================================

    private void mostrarLoading(boolean mostrar) {

        FadeTransition ft = new FadeTransition(Duration.millis(250), panelCarga);

        if (mostrar) {
            panelCarga.setVisible(true);
            ft.setFromValue(0);
            ft.setToValue(1);
        } else {
            ft.setFromValue(panelCarga.getOpacity());
            ft.setToValue(0);
            ft.setOnFinished(e -> panelCarga.setVisible(false));
        }

        ft.play();
    }

    // ======================================================
    //     HOVER ANIMATION
    // ======================================================

    public static void animacionTraslacion(Node node) {
        ScaleTransition stIn = new ScaleTransition(Duration.millis(160), node);
        stIn.setToX(1.04);
        stIn.setToY(1.04);

        ScaleTransition stOut = new ScaleTransition(Duration.millis(160), node);
        stOut.setToX(1);
        stOut.setToY(1);

        node.setOnMouseEntered(e -> stIn.playFromStart());
        node.setOnMouseExited(e -> stOut.playFromStart());
    }
}
