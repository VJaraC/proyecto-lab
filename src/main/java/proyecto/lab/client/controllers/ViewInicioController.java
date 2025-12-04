package proyecto.lab.client.controllers;

import javafx.animation.*;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import proyecto.lab.client.application.AppContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import proyecto.lab.server.controller.EquipoController;
import proyecto.lab.server.dto.SesionHoraDTO;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class ViewInicioController {


    @FXML
    private Button btnCerrarSesion;

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
    private AnchorPane panel5;

    @FXML
    private AnchorPane panel4;

    @FXML
    private AnchorPane panel6;


    @FXML
    void btnCerrarSesion(ActionEvent event) {
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

    @FXML
    void initialize() {
        txtUsuarioSesion.setText((AppContext.getUsuarioActual().getNombres()));
        txtFecha.setText(LocalDate.now().toString());
        setearDashboard();

        //animaciones
        animacionTraslacion(panel1);
        animacionTraslacion(panel2);
        animacionTraslacion(panel3);
        animacionTraslacion(panel4);
        animacionTraslacion(panel5);
        animacionTraslacion(panel6);
    }

        // funciones de dashboard
        public void setearDashboard(){

            //cards
            txtEquiposActivos.setText(String.valueOf((AppContext.equipo().contarEquipoSesionActiva())));
            txtEquiposTotales.setText(String.valueOf(AppContext.equipo().contarEquiposActivos()));

            txtLabsActivos.setText(String.valueOf((AppContext.laboratorio().contarLaboratoriosSesionActiva())));
            txtLabsTotales.setText(String.valueOf(AppContext.laboratorio().contarLaboratoriosActivos()));

            txtSesionesActivas.setText(String.valueOf(AppContext.sesion().contarSesionesActivas()));

            txtAlertas.setText(String.valueOf(AppContext.alerta().contarAlertasRecientes()));

            //ultimas alertas
            Timestamp ts = AppContext.alerta().obtenerUltimaAlerta().getFechaAlerta();
            ZonedDateTime fechaChile = ts.toInstant().atZone(ZoneId.of("America/Santiago"));
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String horaFormateada = fechaChile.format(fmt);
            txtUltimaAlerta.setText(
                    "HORA: " + horaFormateada +
                            "\nEQUIPO: " + AppContext.alerta().obtenerUltimaAlerta().getHostname() + ". ALERTA DE " + AppContext.alerta().obtenerUltimaAlerta().getNombreMetrica() + " : " + AppContext.alerta().obtenerUltimaAlerta().getMensaje());

            //gráfica sesión por hora
            configurarGraficoSesiones();
    }


    private void configurarGraficoSesiones() {
        // Configuración básica del gráfico
        ejeX.setLabel("Hora");
        ejeY.setLabel("Sesiones activas");
        graficoSesiones.setAnimated(false);
        graficoSesiones.setLegendVisible(false);

        // Obtenemos el reporte desde el backend
        // Cada SesionHoraDTO tiene: hora() -> "HH24:00", cantidad() -> nº sesiones
        var reporte = AppContext.sesion().obtenerSesionesPorHora();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sesiones hoy");

        for (SesionHoraDTO dto : reporte) {
            String etiquetaHora = dto.hora();        // ej: "13:00"
            int cantidad = dto.cantidad();           // ej: 5

            series.getData().add(
                    new XYChart.Data<>(etiquetaHora, cantidad)
            );
        }

        graficoSesiones.getData().clear();
        graficoSesiones.getData().add(series);
    }

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
