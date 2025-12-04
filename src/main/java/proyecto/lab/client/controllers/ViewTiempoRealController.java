package proyecto.lab.client.controllers;

import javafx.animation.ScaleTransition;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import proyecto.lab.client.application.AppContext;

import java.io.IOException;
import java.time.LocalDate;

public class ViewTiempoRealController {


    @FXML
    private Button btnCerrarSesion;

    @FXML
    private TableView<?> tablaSesiones;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableColumn<?, ?> txtCpuMetrica;

    @FXML
    private TableColumn<?, ?> txtEquipoMetrica;

    @FXML
    private Label txtIdEquipo;

    @FXML
    private TableColumn<?, ?> txtIdSesionMetrica;

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
}
