package proyecto.lab.client.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.controller.MetricasController;
import proyecto.lab.server.dto.ResumenEquipoDTO;

import java.io.IOException;
import java.time.LocalDate;

public class ViewTiempoRealController {


    @FXML
    private Button btnCerrarSesion;

    @FXML
    private TableView<ResumenEquipoDTO> tablaMetricas;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableColumn<ResumenEquipoDTO, Double> txtCpuMetrica;

    @FXML
    private TableColumn<ResumenEquipoDTO, String> txtEquipoMetrica;

    @FXML
    private Label txtIdEquipo;

    private Timeline autoRefresco;

    @FXML
    private TableColumn<ResumenEquipoDTO, Double> txtRamMetrica;

    @FXML
    private TableColumn<ResumenEquipoDTO, Double> txtDiscoMetrica;

    @FXML
    private Label txtUsuarioSesion;





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
    void initialize(){
        txtUsuarioSesion.setText((AppContext.getUsuarioActual().getNombres()));

        //inicializar tabla

        txtEquipoMetrica.setCellValueFactory(new PropertyValueFactory<>("hostname"));
        txtDiscoMetrica.setCellValueFactory(new PropertyValueFactory<>("diskActual"));
        txtRamMetrica.setCellValueFactory(new PropertyValueFactory<>("ramActual"));
        txtCpuMetrica.setCellValueFactory(new PropertyValueFactory<>("cpuActual"));

        ActualizarTablaMetricas();
        iniciarRefrescoAutomatico();

        }


    private void ActualizarTablaMetricas(){
        tablaMetricas.getItems().clear();
        tablaMetricas.getItems().addAll(AppContext.metricas().obtenerResumenEquipo());
        tablaMetricas.refresh();
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

    @FXML
    void txtBuscar(ActionEvent event) {

    }

    private void iniciarRefrescoAutomatico() {
        autoRefresco = new Timeline(
                new KeyFrame(
                        Duration.seconds(5), // Se actualiza cada 5 segundos
                        event -> {
                            // 1. Guardamos qué equipo estaba seleccionado antes de borrar todo
                            ResumenEquipoDTO seleccionPrevia = tablaMetricas.getSelectionModel().getSelectedItem();
                            String hostnameSeleccionado = (seleccionPrevia != null) ? seleccionPrevia.getHostname() : null;

                            // 2. Refrescamos los datos de la tabla
                            ActualizarTablaMetricas();

                            // 3. Restauramos la selección (para que no se deseleccione solo)
                            if (hostnameSeleccionado != null) {
                                for (ResumenEquipoDTO item : tablaMetricas.getItems()) {
                                    if (item.getHostname().equals(hostnameSeleccionado)) {
                                        tablaMetricas.getSelectionModel().select(item);
                                        break;
                                    }
                                }
                            }
                        }
                )
        );
        autoRefresco.setCycleCount(Animation.INDEFINITE);
        autoRefresco.play();
    }


    }

