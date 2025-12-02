package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import proyecto.lab.server.models.Rol;

public class BaseLayoutController {

    @FXML
    private Button BotonEquipos;

    @FXML
    private Button BotonInicio;

    @FXML
    private Button BotonLaboratorios;

    @FXML
    private Button BotonReportes;

    @FXML
    private Button BotonUsuarios;

    @FXML
    private Button BotonTiempoReal;

    @FXML
    private StackPane ContentArea;

    @FXML
    private AnchorPane anchorLateral;

    private Button activeButton;



    @FXML
    public void initialize() {
        loadView("/views/ViewInicio.fxml");
        setActiveButton(BotonInicio);
    }

    private void loadView(String fxmlPath) {
        try {
            Region view = FXMLLoader.load(getClass().getResource(fxmlPath));
            view.prefWidthProperty().bind(ContentArea.widthProperty());
            view.prefHeightProperty().bind(ContentArea.heightProperty());

            ContentArea.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("activo");
        }
        button.getStyleClass().add("activo");
        activeButton = button;
    }

    @FXML
    void GoEquipos(ActionEvent event) {
        loadView("/views/ViewEquipos.fxml");
        setActiveButton(BotonEquipos);
    }

    @FXML
    void GoInicio(ActionEvent event) {
        loadView("/views/ViewInicio.fxml");
        setActiveButton(BotonInicio);
    }

    @FXML
    void GoLaboratorios(ActionEvent event) {
        loadView("/views/ViewLaboratorios.fxml");
        setActiveButton(BotonLaboratorios);
    }

    @FXML
    void GoReportes(ActionEvent event) {
        loadView("/views/ViewReportes.fxml");
        setActiveButton(BotonReportes);
    }

    @FXML
    void GoUsuarios(ActionEvent event) {
        loadView("/views/ViewUsuarios.fxml");
        setActiveButton(BotonUsuarios);
    }

    @FXML
    void GoTiempoReal(ActionEvent event) {
        loadView("/views/ViewTiempoReal.fxml");
        setActiveButton(BotonTiempoReal);
    }

}
