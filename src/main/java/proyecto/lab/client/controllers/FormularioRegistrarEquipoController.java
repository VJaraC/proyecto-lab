package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.LaboratorioDTO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;

import java.time.LocalDate;

public class FormularioRegistrarEquipoController {

    @FXML
    private Button BotonGuardarEquipo;

    @FXML
    private TextField txtAlmacenamiento;

    @FXML
    private TextField txtFabricante;

    @FXML
    private TextField txtHostname;

    @FXML
    private ComboBox<LaboratorioDTO> txtIdLab;

    @FXML
    private TextField txtMAC;

    @FXML
    private TextField txtIp;

    @FXML
    private TextField txtModelo;

    @FXML
    private TextField txtModeloCpu;

    @FXML
    private TextField txtModeloGpu;

    @FXML
    private TextField txtNucleos;

    @FXML
    private TextField txtNumSerie;

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
    void seleccionarEstado(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        txtEstado.setText(item.getText());
    }

    @FXML
    void GuardarEquipo(ActionEvent e)  {
        Integer idEquipo = 1;
        Integer idLab = 1;  // txtIdLab.getText();
        Integer idUsuario = AppContext.getUsuarioActual().getID();
        String hostname = txtHostname.getText();
        String numSerie = txtNumSerie.getText();
        String fabricante = txtFabricante.getText();
        String estado = txtEstado.getText();
        LocalDate fechaIngreso = LocalDate.now();
        String modelo = txtModelo.getText();
        String mac = txtMAC.getText();
        String ip  = txtIp.getText();
        String modeloCpu = txtModeloCpu.getText();
        String modeloGpu = txtModeloGpu.getText();
        String nucleos = txtNucleos.getText();
        String ram = txtRam.getText();
        String almacenamiento = txtAlmacenamiento.getText();

        try {
            EquipoDTO in = new EquipoDTO(idEquipo, idUsuario, idLab, hostname, numSerie, fabricante, estado, modelo, mac, ip, modeloCpu, nucleos, ram, almacenamiento, modeloGpu, fechaIngreso);
            EquipoDTO creado = AppContext.equipo().crearEquipo(in);  // 💾 BD a través del server

            alert(Alert.AlertType.INFORMATION, "Equipo registrado: " + creado.modelo());
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