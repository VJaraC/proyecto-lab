package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto.lab.server.dto.EquipoDTO;

public class ViewDetalladaEquipoSeleccionadoController {
    private EquipoDTO equipo;
    public void setEquipo(EquipoDTO equipo) {
        this.equipo = equipo;
        refrescar();
    }

    @FXML
    private Button BotonVolver;

    @FXML
    private TextField txtAlmacenamiento;

    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtFabricante;

    @FXML
    private TextField txtFechaIngreso;

    @FXML
    private TextField txtHostname;

    @FXML
    private TextField txtIP;

    @FXML
    private TextField txtIdEquipo;

    @FXML
    private TextField txtMAC;

    @FXML
    private TextField txtModelo;

    @FXML
    private TextField txtModeloCPU;

    @FXML
    private TextField txtNombreLab;

    @FXML
    private TextField txtNucleosCPU;

    @FXML
    private TextField txtNumSerie;

    @FXML
    private TextField txtRAM;

    @FXML
    private TextField txtModeloGPU;

    @FXML
    void Volver(ActionEvent event) {
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        st.close();
    }

    private void cargarCampos() {
        txtIdEquipo.setText(String.valueOf(equipo.id_equipo()));
        txtNombreLab.setText(equipo.nombreLab());
        txtHostname.setText(equipo.hostname());
        txtNumSerie.setText(equipo.numero_serie());
        txtFabricante.setText(equipo.fabricante());
        txtEstado.setText(equipo.estado());
        txtModelo.setText(equipo.modelo());
        txtMAC.setText(equipo.mac());
        txtIP.setText(equipo.ip());
        txtModeloCPU.setText(equipo.modeloCPU());
        txtNucleosCPU.setText(equipo.nucleosCPU());
        txtRAM.setText(equipo.ramTotal());
        txtAlmacenamiento.setText(equipo.almacenamiento());
        txtModeloGPU.setText(equipo.modeloGPU());
        txtFechaIngreso.setText(String.valueOf(equipo.fecha_ingreso()));
    }

    @FXML
    void initialize(){
        if(equipo!=null){
            cargarCampos();
        }

    }

    public void refrescar() { if (equipo != null) cargarCampos(); }


}
