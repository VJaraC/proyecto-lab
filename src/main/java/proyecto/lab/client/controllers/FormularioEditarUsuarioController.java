package proyecto.lab.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.UsuarioDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import proyecto.lab.server.dto.UsuarioUpdateDTO;

public class FormularioEditarUsuarioController {
    private UsuarioDTO usuario;

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
        if (txtNombre != null) {
            cargarCampos();
        }
    }

    @FXML
    private Button GuardarActualizar;

    @FXML
    private TextField txtNombre;

    @FXML
    void GuardarActualizar(ActionEvent event) {
        String nombre = txtNombre.getText();

        try{
            UsuarioUpdateDTO usuarioUpdateDTO = new UsuarioUpdateDTO(usuario.getID(), usuario.getNombres(), usuario.getApellidos(), usuario.getEstado());
            usuario = AppContext.admin().modificarNombreUsuario(usuarioUpdateDTO,nombre);
            alert(Alert.AlertType.INFORMATION, "Usuario modificado: " + usuario.getNombres());
            cerrar(event);

        } catch (RuntimeException ex) { // por validaciones de UsuarioController
            alert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Error al modificar: " + ex.getMessage());
        }}


    private void cerrar(ActionEvent e) {
        Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
        st.close();

    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg).showAndWait();
    }

    private void cargarCampos() {
        txtNombre.setText(usuario.getNombres());
    }

    @FXML
    void initialize() {
        if (usuario != null) {
            cargarCampos();
        }

    }
}

