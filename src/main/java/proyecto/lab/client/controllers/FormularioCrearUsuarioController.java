package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;

public class FormularioCrearUsuarioController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtContrasena;

    @FXML
    void GuardarUsuario(ActionEvent e) {
        String nombre = txtNombre.getText();
        String contr  = txtContrasena.getText();
        try {
            UsuarioLoginDTO in = new UsuarioLoginDTO(nombre, contr);
            UsuarioDTO creado = AppContext.admin().crearUsuario(in);  // 💾 BD a través del server

            alert(Alert.AlertType.INFORMATION, "Usuario creado: " + creado.getNombre());
            cerrar(e);

        } catch (RuntimeException ex) { // por validaciones de AdminController
            alert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Error al crear: " + ex.getMessage());
        }
    }

    @FXML
    void txtNombre(ActionEvent event) {

    }

    @FXML
    void Cancelar(ActionEvent e) { cerrar(e); }

    private void cerrar(ActionEvent e) {
        Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
        st.close();

    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg).showAndWait();
    }

}