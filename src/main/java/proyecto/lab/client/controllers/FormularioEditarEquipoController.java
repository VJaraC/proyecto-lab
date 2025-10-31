package proyecto.lab.client.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.*;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.models.Rol;

import java.time.LocalDate;

public class FormularioEditarEquipoController {
    private EquipoDTO equipo;

    public void setEquipo(EquipoDTO equipo) {
        this.equipo = equipo;
        if (txtModeloCpu != null) {
            cargarCampos();
        }
    }

    @FXML
    private Button BotonActualizarEquipo;

    @FXML
    private TextField txtAlmacenamiento;

    @FXML
    private TextField txtHostname;

    @FXML
    private ChoiceBox<LaboratorioDTO> txtIdLab;

    @FXML
    private TextField txtIp;

    @FXML
    private TextField txtModeloCpu;

    @FXML
    private TextField txtModeloGpu;

    @FXML
    private TextField txtNucleos;

    @FXML
    private TextField txtRam;

    @FXML
    private MenuButton txtEstado;

    @FXML
    private MenuItem disponible;

    @FXML
    private MenuItem operativo;

    @FXML
    private MenuItem fueraDeServicio;


    @FXML
    void GuardarActualizarEquipo(ActionEvent event) {
        String hostname = txtHostname.getText();
        String estado = txtEstado.getText();
        String ip  = txtIp.getText();
        String modeloCpu = txtModeloCpu.getText();
        String modeloGpu = txtModeloGpu.getText();
        String nucleos = txtNucleos.getText();
        String ram = txtRam.getText();
        String almacenamiento = txtAlmacenamiento.getText();
        //LaboratorioDTO labSel = txtIdLab.getValue();

        boolean cambios = false;

        try {
            if ((equipo.hostname() == null && hostname != null) ||
                    (equipo.hostname() != null && !equipo.hostname().equals(hostname))) {
                EquipoUpdateDTO dto = dtoSoloId();
                equipo = AppContext.equipo().modificarHostname(dto, hostname);
                cambios = true;
            }

            if ((equipo.ip() == null && ip != null) ||
                    (equipo.ip() != null && !equipo.ip().equals(ip))) {
                EquipoUpdateDTO dto = dtoSoloId();
                equipo = AppContext.equipo().modificarIP(dto, ip);
                cambios = true;
            }

            if ((equipo.modeloCPU() == null && modeloCpu != null) ||
                    (equipo.modeloCPU() != null && !equipo.modeloCPU().equals(modeloCpu))) {
                EquipoUpdateDTO dto = dtoSoloId();
                equipo = AppContext.equipo().modificarModeloCPU(dto, modeloCpu);
                cambios = true;
            }

            if ((equipo.nucleosCPU() == null && nucleos != null) ||
                    (equipo.nucleosCPU() != null && !equipo.nucleosCPU().equals(nucleos))) {
                EquipoUpdateDTO dto = dtoSoloId();
                equipo = AppContext.equipo().modificarNucleosCPU(dto, nucleos);
                cambios = true;
            }

            if ((equipo.ramTotal() == null && ram != null) ||
                    (equipo.ramTotal() != null && !equipo.ramTotal().equals(ram))) {
                EquipoUpdateDTO dto = dtoSoloId();
                equipo = AppContext.equipo().modificarRamTotal(dto, ram);
                cambios = true;
            }

            if ((equipo.almacenamiento() == null && almacenamiento != null) ||
                    (equipo.almacenamiento() != null && !equipo.almacenamiento().equals(almacenamiento))) {
                EquipoUpdateDTO dto = dtoSoloId();
                equipo = AppContext.equipo().modificarAlmacenamiento(dto, almacenamiento);
                cambios = true;
            }

            if ((equipo.modeloGPU() == null && modeloGpu != null) ||
                    (equipo.modeloGPU() != null && !equipo.modeloGPU().equals(modeloGpu))) {
                EquipoUpdateDTO dto = dtoSoloId();
                equipo = AppContext.equipo().modificarModeloGPU(dto, modeloGpu);
                cambios = true;
            }

            if ((equipo.estado() == null && estado != null) ||
                    (equipo.estado() != null && !equipo.estado().equals(estado))) {
                EquipoUpdateDTO dto = dtoSoloId();
                equipo = AppContext.equipo().modificarEstado(dto, estado);
                cambios = true;
            }

            //if ((equipo.id_lab_equipo() == null && idLab != null)) ||
            //(equipo.id_lab_equipo() != null && !equipo.id_lab_equipo().equals(idLab){
            //
            //}


            if (!cambios) {
                alert(Alert.AlertType.INFORMATION, "No hay cambios para aplicar.");
                return;
            }

            alert(Alert.AlertType.INFORMATION, "Equipo ID: " + equipo.id_equipo() + "modificado correctamente");
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
        txtHostname.setText(equipo.hostname());
        txtIp.setText(equipo.ip());
        txtModeloCpu.setText(equipo.modeloCPU());
        txtNucleos.setText(equipo.nucleosCPU());
        txtModeloGpu.setText(equipo.modeloGPU());
        txtRam.setText(equipo.ramTotal());
        txtAlmacenamiento.setText(equipo.almacenamiento());
        txtEstado.setText(equipo.estado());

        //var labs = AppContext.laboratorio().listarLaboratorios();
        //txtIdLab.setItems(FXCollections.observableArrayList(labs));

        // Pre-relleno por id
        //if (equipo != null && equipo.id_lab_equipo() != null) {
        //    labs.stream()
        //            .filter(l -> l.id_lab() == equipo.id_lab_equipo())
        //            .findFirst()
        //            .ifPresent(chLab::setValue);
        //}
    }
    @FXML
    void estadoSeleccionado(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        txtEstado.setText(item.getText());
    }

    private EquipoUpdateDTO dtoSoloId() {
        return new EquipoUpdateDTO(equipo.id_equipo(), null, null, null, null, null, null, null, null);
    }


    @FXML
    void initialize() {
        if (equipo != null) {
            cargarCampos();
        }

    }

}

