package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.LaboratorioUpdateDTO;
import proyecto.lab.server.dto.LaboratorioDTO;
import java.util.Objects;

import java.time.LocalDate;

public class FormularioEditarLaboratorioController {
    private LaboratorioDTO laboratorio;

    public void setLaboratorio(LaboratorioDTO laboratorio) {
        this.laboratorio = laboratorio;
        if (txtNombre != null) {
            cargarCampos();
        }
    }

    @FXML
    private Button BotonGuardarLaboratorio;

    @FXML
    private MenuItem deshabilitado;

    @FXML
    private MenuItem habilitado;

    @FXML
    private TextField txtCapacidadEquipos;

    @FXML
    private TextField txtCapacidadPersonas;

    @FXML
    private MenuButton txtEstado;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtUbicacion;

    @FXML
    void seleccionarEstado(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        txtEstado.setText(item.getText());
    }



    @FXML
    void GuardarActualizarLaboratorio(ActionEvent event) {
        String nombre_lab = txtNombre.getText();
        String ubicacion = txtUbicacion.getText();
        Integer capacidad_personas = Integer.valueOf(txtCapacidadPersonas.getText());
        Integer capacidad_equipos = Integer.valueOf(txtCapacidadEquipos.getText());
        String estado_lab = txtEstado.getText();

        boolean cambios = false;

        try {
            if ((laboratorio.nombre_lab() == null && nombre_lab != null) ||
                    (laboratorio.nombre_lab() != null && !laboratorio.nombre_lab().equals(nombre_lab))) {
                LaboratorioUpdateDTO dto = dtoSoloId();
                laboratorio = AppContext.laboratorio().modificarNombre(dto, nombre_lab);
                cambios = true;
            }

            if ((laboratorio.ubicacion() == null && ubicacion != null) ||
                    (laboratorio.ubicacion() != null && !laboratorio.ubicacion().equals(ubicacion))) {
                LaboratorioUpdateDTO dto = dtoSoloId();
                laboratorio = AppContext.laboratorio().modificarUbicacion(dto, ubicacion);
                cambios = true;
            }

            if (!Objects.equals(laboratorio.capacidad_personas(), capacidad_personas)) {
                LaboratorioUpdateDTO dto = dtoSoloId();
                laboratorio = AppContext.laboratorio().modificarCapacidadPersonas(dto, capacidad_personas);
                cambios = true;
            }

            if (!Objects.equals(laboratorio.capacidad_equipo(), capacidad_equipos)) {
                LaboratorioUpdateDTO dto = dtoSoloId();
                laboratorio = AppContext.laboratorio().modificarCapacidadEquipos(dto, capacidad_equipos);
                cambios = true;
            }

            if ((laboratorio.estado_lab() == null && estado_lab != null) ||
                    (laboratorio.estado_lab() != null && !laboratorio.estado_lab().equals(estado_lab))) {
                LaboratorioUpdateDTO dto = dtoSoloId();
                laboratorio = AppContext.laboratorio().modificarEstado(dto, estado_lab);
                cambios = true;
            }


            if (!cambios) {
                alert(Alert.AlertType.INFORMATION, "No hay cambios para aplicar.");
                return;
            }

            alert(Alert.AlertType.INFORMATION, "laboratorio ID: " + laboratorio.id_laboratorio() + "modificado correctamente");
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
        txtNombre.setText(laboratorio.nombre_lab());
        txtEstado.setText(laboratorio.estado_lab());
        txtUbicacion.setText(laboratorio.ubicacion());
        txtCapacidadPersonas.setText(String.valueOf(laboratorio.capacidad_personas()));
        txtCapacidadEquipos.setText(String.valueOf(laboratorio.capacidad_equipo()));
    }


    private LaboratorioUpdateDTO dtoSoloId() {
        return new LaboratorioUpdateDTO(laboratorio.id_lab(), null, null, null);
    }


    @FXML
    void initialize() {
        if (laboratorio != null) {
            cargarCampos();
        }

    }

}

