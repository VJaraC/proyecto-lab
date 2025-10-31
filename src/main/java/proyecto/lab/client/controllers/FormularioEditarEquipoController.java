package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.UsuarioUpdateDTO;
import proyecto.lab.server.models.Rol;

public class FormularioEditarEquipoController {
    private EquipoDTO equipo;

    public void setEquipo(EquipoDTO equipo) {
        this.equipo = equipo;
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
        String nombre    = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String cargo     = txtCargo.getText();
        String telefono  = txtTelefono.getText();
        String email     = txtCorreo.getText();
        String rolTexto  = txtRol.getText();

        boolean cambios = false;

        try {
            if ((usuario.getNombres() == null && nombre != null) ||
                    (usuario.getNombres() != null && !usuario.getNombres().equals(nombre))) {
                UsuarioUpdateDTO dto = dtoSoloId();
                usuario = AppContext.admin().modificarNombreUsuario(dto, nombre, AppContext.getUsuarioActual());
                cambios = true;
            }

            if ((usuario.getApellidos() == null && apellidos != null) ||
                    (usuario.getApellidos() != null && !usuario.getApellidos().equals(apellidos))) {
                UsuarioUpdateDTO dto = dtoSoloId();
                usuario = AppContext.admin().modificarApellidoUsuario(dto, apellidos, AppContext.getUsuarioActual());
                cambios = true;
            }

            if ((usuario.getEmail() == null && email != null) ||
                    (usuario.getEmail() != null && !usuario.getEmail().equals(email))) {
                UsuarioUpdateDTO dto = dtoSoloId();
                usuario = AppContext.admin().modificarCorreoUsuario(dto, email, AppContext.getUsuarioActual());
                cambios = true;
            }

            if ((usuario.getTelefono() == null && telefono != null) ||
                    (usuario.getTelefono() != null && !usuario.getTelefono().equals(telefono))) {
                UsuarioUpdateDTO dto = dtoSoloId();
                usuario = AppContext.admin().modificarTelefonoUsuario(dto, telefono, AppContext.getUsuarioActual());
                cambios = true;
            }

            if ((usuario.getCargo() == null && cargo != null) ||
                    (usuario.getCargo() != null && !usuario.getCargo().equals(cargo))) {
                UsuarioUpdateDTO dto = dtoSoloId();
                usuario = AppContext.admin().modificarCargoUsuario(dto, cargo, AppContext.getUsuarioActual());
                cambios = true;
            }

            if (rolTexto != null && !rolTexto.isBlank()) {
                Rol rolNuevo = Rol.valueOf(rolTexto.trim().toUpperCase());
                if (usuario.getRol() != rolNuevo) {
                    UsuarioUpdateDTO dto = dtoSoloId();
                    usuario = AppContext.admin().actualizarRolUsuario(dto, AppContext.getUsuarioActual());
                    cambios = true;
                }
            }

            if (!cambios) {
                alert(Alert.AlertType.INFORMATION, "No hay cambios para aplicar.");
                return;
            }

            alert(Alert.AlertType.INFORMATION, "Usuario modificado: " + usuario.getNombres() + " "+ usuario.getApellidos());
            cerrar(event);

        } catch (RuntimeException ex) {
            alert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Error al modificar: " + ex.getMessage());
        }
    }



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
        txtRol.setText(String.valueOf(usuario.getRol()));
    }

    @FXML
    void rolSeleccionado(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        txtRol.setText(item.getText());
    }

    private UsuarioUpdateDTO dtoSoloId() {
        return new UsuarioUpdateDTO(usuario.getID(), null, null, null, null, null, null, null, null);
    }


    @FXML
    void initialize() {
        if (usuario != null) {
            cargarCampos();
        }

    }
}

