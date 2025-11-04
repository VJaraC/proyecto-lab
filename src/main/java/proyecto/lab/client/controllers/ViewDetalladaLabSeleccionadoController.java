package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto.lab.server.dto.LaboratorioDTO;
import proyecto.lab.server.dto.UsuarioDTO;

public class ViewDetalladaLabSeleccionadoController {
    private LaboratorioDTO lab;
    public void setLab(LaboratorioDTO lab) {
        this.lab = lab;
        refrescar();
    }

    @FXML
    private Button BotonVolver;

    @FXML
    private TextField txtCapacidadEquipos;

    @FXML
    private TextField txtCapacidadPersonas;

    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtFechaRegistro;

    @FXML
    private TextField txtIdLab;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtUbicacion;

    @FXML
    void Volver(ActionEvent event) {
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        st.close();
    }

    private void cargarCampos() {
        txtIdLab.setText(String.valueOf(lab.id_lab()));
        txtNombre.setText(lab.nombre_lab());
        txtUbicacion.setText(lab.ubicacion());
        txtCapacidadPersonas.setText(String.valueOf(lab.capacidad_personas()));
        txtCapacidadEquipos.setText(String.valueOf(lab.capacidad_equipo()));
        txtEstado.setText(lab.estado_lab());
        txtFechaRegistro.setText(String.valueOf(lab.fecha_registro_lab()));
    }

    @FXML
    void initialize(){
        if(lab!=null){
            cargarCampos();
        }

    }

    public void refrescar() { if (lab != null) cargarCampos(); }


}
