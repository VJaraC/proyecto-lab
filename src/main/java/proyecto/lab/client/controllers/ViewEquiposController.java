package proyecto.lab.client.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import proyecto.lab.client.application.AppContext;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.EquipoUpdateDTO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.models.Rol;
import java.io.IOException;
import java.util.Optional;

public class ViewEquiposController {

    @FXML
    void AbrirFormularioRegistrarEquipo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioRegistrarEquipo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar un equipo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal hasta cerrar
            stage.showAndWait();
            ActualizarTablaEquipo();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private SplitMenuButton Buscar;

    private String FiltroSeleccionado;

    @FXML
    private Button BotonRegistrarEquipo;

    @FXML
    private MenuItem FiltroEstado;

    @FXML
    private MenuItem FiltroID;

    @FXML
    private MenuItem FiltroNumSerie;

    @FXML
    private MenuItem FiltroIdLab;

    @FXML
    private MenuItem FiltroHostname;

    @FXML
    private MenuItem FiltroFabricante;

    @FXML
    private MenuItem FiltroModelo;

    @FXML
    private MenuItem FiltroMac;

    @FXML
    private Label txtUsuarioSesion;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableColumn<EquipoDTO, String> EstadoTablaEquipo;

    @FXML
    private TableColumn<EquipoDTO, Integer> IdTablaEquipo;

    @FXML
    private TableColumn<EquipoDTO, String> NumSerieTablaEquipo;

    @FXML
    private TableColumn<EquipoDTO, String> LabTablaEquipo;

    @FXML
    private TableColumn<EquipoDTO, Void> AccionTablaEquipo;

    @FXML
    private TableColumn<EquipoDTO, String> ModeloTablaEquipo;

    @FXML
    private TableView<EquipoDTO> TablaEquipo;



    private void ActualizarTablaEquipo(){
        TablaEquipo.getItems().clear();
        TablaEquipo.getItems().addAll(AppContext.equipo().listarEquipos());
        TablaEquipo.refresh();
    }

    private void LimpiarTablaEquipo(){
        TablaEquipo.getItems().clear();
        TablaEquipo.refresh();
    }


    @FXML
    void initialize(){
        IdTablaEquipo.setCellValueFactory(cd ->
                new ReadOnlyObjectWrapper<>(cd.getValue().id_equipo()));

        NumSerieTablaEquipo.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper(cd.getValue().numero_serie()));

        // Muestra nombre del lab si viene; si no, al menos el ID de lab
        LabTablaEquipo.setCellValueFactory(cd -> {
            var e = cd.getValue();
            String texto = Optional.ofNullable(e.nombreLab())
                    .orElse("ID " + e.id_lab_equipo());
            return new ReadOnlyStringWrapper(texto);
        });

        ModeloTablaEquipo.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper(cd.getValue().modelo()));

        EstadoTablaEquipo.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper(cd.getValue().estado()));

        boolean esAdmin = AppContext.getUsuarioActual().getRol() == Rol.ADMIN;
        if (!esAdmin && BotonRegistrarEquipo != null) {
            BotonRegistrarEquipo.setVisible(false);
            BotonRegistrarEquipo.setManaged(false); // colapsa el espacio
        }

        configurarColumnaAccion();
        configurarColumnaEstado();
        ActualizarTablaEquipo();
        txtUsuarioSesion.setText((AppContext.getUsuarioActual().getNombres()));

        for (MenuItem item : Buscar.getItems()) {
            item.addEventHandler(ActionEvent.ACTION, e -> {
                Buscar.setText(item.getText());
                if (!Buscar.getStyleClass().contains("activo")) {
                    Buscar.getStyleClass().add("activo");
                }
                e.consume(); // evita que el evento se propague a otra acción del SplitMenuButton
            });
        }

        TablaEquipo.setRowFactory(tv -> {
            TableRow<EquipoDTO> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && !row.isEmpty()) {
                    AbrirVistaDetalladaEquipo(row.getItem());
                }
            });
            return row;
        });
    }

    private void AbrirVistaDetalladaEquipo(EquipoDTO equipoSeleccionado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViewDetalladaEquipoSeleccionado.fxml"));
            Parent root = loader.load();
            ViewDetalladaEquipoSeleccionadoController controller = loader.getController();
            controller.setEquipo(equipoSeleccionado);
            Stage stage = new Stage();
            stage.setTitle("Detalle del Equipo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configurarColumnaEstado() {
        EstadoTablaEquipo.setCellFactory(col -> new TableCell<EquipoDTO, String>() {
            private final Label label = new Label();{
                label.getStyleClass().add("texto");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                label.setText(item);
                label.getStyleClass().remove("disponible");
                label.getStyleClass().remove("deshabilitado");

                if ("disponible".equalsIgnoreCase(item)) {
                    label.getStyleClass().add("habilitado");
                } else if("fuera de servicio".equalsIgnoreCase(item)) {
                    label.getStyleClass().add("deshabilitado");
                }else if("operativo".equalsIgnoreCase(item)) {
                    label.getStyleClass().add("habilitado");
                }
                setAlignment(Pos.CENTER);
                setGraphic(label);
                setText(null);
            }
        });
    }


    void AbrirFormularioEditarEquipo(EquipoDTO equipoSeleccionado) {
        try {
            EquipoDTO completo = AppContext.equipo().buscarEquipoPorId(equipoSeleccionado.id_equipo());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioEditarEquipo.fxml"));
            Parent root = loader.load();
            FormularioEditarEquipoController controller = loader.getController();
            controller.setEquipo(completo);

            Stage stage = new Stage();
            stage.setTitle("Editar Equipo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal hasta cerrar
            stage.showAndWait();
            ActualizarTablaEquipo();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configurarColumnaAccion() {
        AccionTablaEquipo.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = crearBotonAccion("Ver");
            private final Button btnEditar = crearBotonAccion("Editar");
            private final HBox box = new HBox(6);

            {
                btnVer.setOnAction(e -> {
                    EquipoDTO equipo = getTableView().getItems().get(getIndex());
                    AbrirVistaDetalladaEquipo(equipo);
                });

                btnEditar.setOnAction(e -> {
                    EquipoDTO equipo = getTableView().getItems().get(getIndex());
                    AbrirFormularioEditarEquipo(equipo);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0) {
                    setGraphic(null);
                    return;
                }

                boolean esAdmin = AppContext.getUsuarioActual().getRol() == Rol.ADMIN;

                if (esAdmin) {
                    // ADMIN: puede ver y editar
                    box.getChildren().setAll(btnVer, btnEditar);
                } else {
                    // MONITOR: solo puede ver
                    box.getChildren().setAll(btnVer);
                }

                setGraphic(box);
            }
        });
    }

    public Button crearBotonAccion(String texto){
        Button button = new Button(texto);
        button.getStyleClass().add("botonAccion");
        return button;
    }

    private EquipoUpdateDTO crearUpdateDTO(EquipoDTO equipo) {
        int id = equipo.id_equipo();
        String estado =  equipo.estado();
        return new EquipoUpdateDTO(id, null,null, null, estado, null, null, null, null, null);
    }


    @FXML
    void Buscar(ActionEvent event) {
        String busqueda = txtBuscar.getText();

        Buscar.getStyleClass().remove("activo");
        try{

            if (FiltroSeleccionado == null) {
                alert(Alert.AlertType.WARNING, "Por favor, selecciona un filtro antes de buscar.");
                return;
            }
            switch (FiltroSeleccionado) {
                    case "estado":
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.equipo().buscarEquipoPorEstado(busqueda));
                        break;
                    case "numSerie":
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.equipo().buscarEquipoPorNumSerie(busqueda));
                        break;
                    case "ID":
                        Integer idEquipo = null;
                        if (busqueda != null && !busqueda.isBlank()) {
                            idEquipo = Integer.valueOf(busqueda);
                        }
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.equipo().buscarEquipoPorId(idEquipo));
                        break;
                    case "idLab":
                        Integer idLab = null;
                        if (busqueda != null && !busqueda.isBlank()) {
                            idLab = Integer.valueOf(busqueda);
                        }
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.equipo().buscarEquipoPorIdLab(idLab));
                        break;
                    case "hostname":
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.equipo().buscarEquipoPorHostname(busqueda));
                        break;
                    case "fabricante":
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.equipo().buscarEquipoPorFabricante(busqueda));
                        break;
                    case "modelo":
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.equipo().buscarEquipoPorModelo(busqueda));
                        break;
                    case "mac":
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.equipo().buscarEquipoPorMac(busqueda));
                        break;



            }

        } catch (RuntimeException ex) {// por validaciones de UsuarioController
            ActualizarTablaEquipo();
            alert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, ex.getMessage());
        }

    }

    @FXML
    String FiltroEstado(ActionEvent event) {
        FiltroSeleccionado = "estado";
        Buscar.setText("Buscar por Estado");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroID(ActionEvent event) {
        FiltroSeleccionado = "ID";
        Buscar.setText("Buscar por ID");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroNumSerie(ActionEvent event) {
        FiltroSeleccionado = "numSerie";
        Buscar.setText("Buscar por Numero de Serie");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroIdLab(ActionEvent event) {
        FiltroSeleccionado = "idLab";
        Buscar.setText("Buscar por ID de Laboratorio");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroHostname(ActionEvent event) {
        FiltroSeleccionado = "hostname";
        Buscar.setText("Buscar por Hostname");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroFabricante(ActionEvent event) {
        FiltroSeleccionado = "fabricante";
        Buscar.setText("Buscar por Fabricante");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroModelo(ActionEvent event) {
        FiltroSeleccionado = "modelo";
        Buscar.setText("Buscar por Modelo");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroMac(ActionEvent event) {
        FiltroSeleccionado = "mac";
        Buscar.setText("Buscar por Mac");
        return FiltroSeleccionado;
    }



    @FXML
    void txtBuscar(ActionEvent event) {

    }


    @FXML
    void btnCerrarSesion(ActionEvent event) {
            AppContext.LimpiarSesion();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/IniciarSesion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Reemplazar la escena actual con la de Login
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Monitoreo - UNAP");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg).showAndWait();
    }

}



