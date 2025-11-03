package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.LaboratorioDTO;

import java.time.LocalDate;

public class FormularioRegistrarLaboratorioController {

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
    void GuardarLaboratorio(ActionEvent e) {
        Integer idUsuario = AppContext.getUsuarioActual().getID();
        String nombre_lab = txtNombre.getText();
        String ubicacion = txtUbicacion.getText();
        Integer capacidad_personas = Integer.valueOf(txtCapacidadPersonas.getText());
        Integer capacidad_equipos = Integer.valueOf(txtCapacidadEquipos.getText());
        String estado = txtEstado.getText();
        LocalDate fechaIngreso = LocalDate.now();

        try {
            LaboratorioDTO in = new LaboratorioDTO(id_lab, idUsuario, nombre_lab, ubicacion, capacidad_personas, capacidad_equipos, estado, fechaIngreso);
            LaboratorioDTO creado = AppContext.laboratorio().crearLaboratorio(in);  // 💾 BD a través del server

            alert(Alert.AlertType.INFORMATION, "Laboratorio registrado: " + creado.nombre_lab());
            cerrar(e);

        } catch (RuntimeException ex) { // por validaciones de EquipoController
            alert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, "Error al crear: " + ex.getMessage());
        }
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