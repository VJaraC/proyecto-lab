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
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import proyecto.lab.client.application.AppContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.LocalDate;

public class ViewInicioController{


    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Label txtUsuarioSesion;

    @FXML
    private CategoryAxis ejeX;

    @FXML
    private NumberAxis ejeY;

    @FXML
    private LineChart<String, Integer> graficoSesiones;

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
    private Label txtSesionesTotales;

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
    void initialize(){
        txtUsuarioSesion.setText((AppContext.getUsuarioActual().getNombres()));
        txtFecha.setText(LocalDate.now().toString());
        setearEstadoServidor();

        //animaciones
        animacionTraslacion(panel1);
        animacionTraslacion(panel2);
        animacionTraslacion(panel3);
        animacionTraslacion(panel4);
        animacionTraslacion(panel5);
        animacionTraslacion(panel6);
        }


        public void setearEstadoServidor(){
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
