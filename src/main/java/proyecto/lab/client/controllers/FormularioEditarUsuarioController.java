package proyecto.lab.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.UsuarioDTO;
import javafx.event.ActionEvent;
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
    private MenuItem rolAdministrador;

    @FXML
    private MenuItem rolMonitor;

    @FXML
    private MenuButton txtRol;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCargo;

    @FXML
    private TextField txtContrasena;

    @FXML
    private TextField txtCorreo;


    @FXML
    private TextField txtTelefono;




    @FXML
    void GuardarActualizar(ActionEvent event) {
        String nombre = txtNombre.getText();

        try{
            UsuarioUpdateDTO usuarioUpdateDTO = new UsuarioUpdateDTO(usuario.getID(), usuario.getNombres(), usuario.getApellidos(), usuario.getEstado(), null, null, null, null, null);

            usuario = AppContext.admin().modificarNombreUsuario(usuarioUpdateDTO,nombre,AppContext.getUsuarioActual());
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
        txtApellidos.setText(usuario.getApellidos());
        txtCargo.setText(usuario.getCargo());
        txtTelefono.setText(usuario.getTelefono());
        txtCorreo.setText(usuario.getEmail());
        //txtContrasena.setText(usuario.getContrasena);
        txtRol.setText("Rol actual: "+ usuario.getRol());
    }

    @FXML
    void rolSeleccionado(ActionEvent event) {

    }


    @FXML
    void initialize() {
        if (usuario != null) {
            cargarCampos();
        }

    }
}

