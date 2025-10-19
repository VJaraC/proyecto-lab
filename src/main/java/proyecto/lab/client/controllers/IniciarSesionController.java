package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;

import java.io.IOException;
import java.util.Objects;


public class IniciarSesionController {
    @FXML
    private PasswordField txtContrasena;

    @FXML
    private TextField txtRUT;

    @FXML
    private Button IniciarSesionBtn;

    @FXML
    void IniciarSesion(ActionEvent event) {
        UsuarioLoginDTO cred = new UsuarioLoginDTO(txtRUT.getText(),txtContrasena.getText());

        try{
            AppContext.admin().iniciarSesion(cred);
            UsuarioDTO usuario = AppContext.admin().iniciarSesion(cred);
            AppContext.setUsuarioActual(usuario);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/BaseLayout.fxml")));
            var stage = (Stage) IniciarSesionBtn.getScene().getWindow();
            stage.setTitle("Sistema de Monitoreo - UNAP");
            stage.setScene(new Scene(root));
            stage.show();

        }catch(RuntimeException ex){
            alert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void animarHoverEntrada(MouseEvent event) {
    }

    @FXML
    void animarHoverSalida(MouseEvent event) {
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg).showAndWait();
    }
}
