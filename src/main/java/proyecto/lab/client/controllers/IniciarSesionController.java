package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.UsuarioLoginDTO;


public class IniciarSesionController {
    @FXML
    private PasswordField txtContrasena;

    @FXML
    private TextField txtNombre;

    @FXML
    private Button IniciarSesionBtn;

    @FXML
    void IniciarSesion(ActionEvent event) {
        UsuarioLoginDTO usuario = new UsuarioLoginDTO(txtNombre.getText(),txtContrasena.getText());

        try{
            AppContext.admin().iniciarSesion(usuario);

        }catch(RuntimeException ex){
            alert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, ex.getMessage());
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
