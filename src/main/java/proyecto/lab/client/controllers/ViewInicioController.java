package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ViewInicioController{


    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Label txtUsuarioSesion;

@FXML
void btnCerrarSesion(ActionEvent event) {
    AppContext.LimpiarSesion();

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/IniciarSesion.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // 4. Reemplazar la escena actual con la de Login
        stage.setScene(new Scene(root));
        stage.setTitle("Sistema de Monitoreo - UNAP");
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    void initialize(){;
        txtUsuarioSesion.setText((AppContext.getUsuarioActual().getNombres()));
        }
}
