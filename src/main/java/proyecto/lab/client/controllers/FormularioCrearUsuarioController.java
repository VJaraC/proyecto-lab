package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;

import java.time.LocalDate;

public class FormularioCrearUsuarioController {

    @FXML
    private MenuItem GeneroFemenino;

    @FXML
    private MenuItem GeneroMasculino;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCargo;

    @FXML
    private TextField txtContrasena;

    @FXML
    private TextField txtCorreo;

    @FXML
    private DatePicker txtFechaNac;

    @FXML
    private MenuButton txtGenero;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtRUT;

    @FXML
    private TextField txtTelefono;

    @FXML
    void seleccionarGenero(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        txtGenero.setText(item.getText());
    }


        @FXML
    void GuardarUsuario(ActionEvent e) {
        String rut = txtRUT.getText();
        String nombres = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String genero = txtGenero.getText();
        String cargo = txtCargo.getText();
        LocalDate fecha_nacimiento = txtFechaNac.getValue();
        String telefono = txtTelefono.getText();
        String email = txtCorreo.getText();
        String contr  = txtContrasena.getText();
        try {
            UsuarioLoginDTO in = new UsuarioLoginDTO(rut, nombres, apellidos, genero, contr, cargo, fecha_nacimiento, telefono, email);
            UsuarioDTO creado = AppContext.admin().crearUsuario(in,AppContext.getUsuarioActual());  // 💾 BD a través del server

            alert(Alert.AlertType.INFORMATION, "Usuario creado: " + creado.getNombres());
            cerrar(e);

        } catch (RuntimeException ex) { // por validaciones de UsuarioController
            alert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Error al crear: " + ex.getMessage());
        }
    }

    @FXML
    void txtRUT(ActionEvent event) {

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