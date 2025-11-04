package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto.lab.server.dto.UsuarioDTO;

public class ViewDetalladaUsuarioSeleccionadoController {
    private UsuarioDTO usuario;
    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
        refrescar();
    }

    @FXML
    private Button BotonVolver;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCargo;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtFechaNac;

    @FXML
    private TextField txtGenero;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtNombres;

    @FXML
    private TextField txtRUT;

    @FXML
    private TextField txtRol;

    @FXML
    private TextField txtTelefono;

    @FXML
    void Volver(ActionEvent event) {
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        st.close();
    }

    private void cargarCampos() {
        txtID.setText(String.valueOf(usuario.getID()));
        txtRUT.setText(usuario.getRut());
        txtNombres.setText(usuario.getNombres());
        txtApellidos.setText(usuario.getApellidos());
        txtEmail.setText(usuario.getEmail());
        txtEstado.setText(usuario.getEstado());
        txtGenero.setText(usuario.getGenero());
        txtCargo.setText(usuario.getCargo());
        txtRol.setText(String.valueOf(usuario.getRol()));
        txtTelefono.setText(usuario.getTelefono());
        txtFechaNac.setText(String.valueOf(usuario.getFechaNacimiento()));
    }

    @FXML
    void initialize(){
        if(usuario!=null){
            cargarCampos();
        }

    }

    public void refrescar() { if (usuario != null) cargarCampos(); }


}
