package proyecto.lab.client.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import proyecto.lab.server.dto.LaboratorioDTO;
import proyecto.lab.server.dto.LaboratorioUpdateDTO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.models.Rol;

import java.io.IOException;

public class ViewLaboratoriosController {

    @FXML
    void AbrirFormularioRegistrarLaboratorio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioRegistrarLaboratorio.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar un Laboratorio");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal hasta cerrar
            stage.showAndWait();
            ActualizarTablaLaboratorio();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private SplitMenuButton Buscar;

    private String FiltroSeleccionado;

    @FXML
    private Button BotonRegistrarLaboratorio;

    @FXML
    private TableColumn<LaboratorioDTO, Void> AccionTablaLaboratorio;


    @FXML
    private TableColumn<LaboratorioDTO, String> EstadoTablaLaboratorio;

    @FXML
    private MenuItem FiltroCapacidadEquipos;

    @FXML
    private MenuItem FiltroCapacidadPersonas;

    @FXML
    private MenuItem FiltroEstado;

    @FXML
    private MenuItem FiltroID;

    @FXML
    private MenuItem FiltroNombre;

    @FXML
    private MenuItem FiltroUbicacion;

    @FXML
    private TableColumn<LaboratorioDTO, Integer> IdTablaLaboratorio;

    @FXML
    private TableColumn<LaboratorioDTO, String> NombreTablaLaboratorio;

    @FXML
    private TableView<LaboratorioDTO> TablaLaboratorio;

    @FXML
    private TableColumn<LaboratorioDTO, String> UbicacionTablaLaboratorio;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label txtUsuarioSesion;




    private void ActualizarTablaLaboratorio(){
        TablaLaboratorio.getItems().clear();
        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().listarLaboratorios(AppContext.getUsuarioActual()));
        TablaLaboratorio.refresh();
    }

    private void LimpiarTablaLaboratorio(){
        TablaLaboratorio.getItems().clear();
        TablaLaboratorio.refresh();
    }


    @FXML
    void initialize() {
        IdTablaLaboratorio.setCellValueFactory(cd ->
                new ReadOnlyObjectWrapper<>(cd.getValue().id_lab()));
        NombreTablaLaboratorio.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper(cd.getValue().nombre_lab()));
        UbicacionTablaLaboratorio.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper(cd.getValue().ubicacion()));
        EstadoTablaLaboratorio.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper(cd.getValue().estado_lab()));

        configurarColumnaAccion();
        configurarColumnaEstado();
        ActualizarTablaLaboratorio();
        txtUsuarioSesion.setText(AppContext.getUsuarioActual().getNombres());

        boolean esAdmin = AppContext.getUsuarioActual().getRol() == Rol.ADMIN;
        if (!esAdmin && BotonRegistrarLaboratorio != null) {
            BotonRegistrarLaboratorio.setVisible(false);
            BotonRegistrarLaboratorio.setManaged(false); // colapsa el espacio
        }

        for (MenuItem item : Buscar.getItems()) {
            item.addEventHandler(ActionEvent.ACTION, e -> {
                Buscar.setText(item.getText());
                if (!Buscar.getStyleClass().contains("activo")) {
                    Buscar.getStyleClass().add("activo");
                }
                e.consume(); // evita que el evento se propague a otra acción del SplitMenuButton
            });
        }

        TablaLaboratorio.setRowFactory(tv -> {
            TableRow<LaboratorioDTO> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && !row.isEmpty()) {
                    AbrirVistaDetalladaLaboratorio(row.getItem());
                }
            });
            return row;
        });
    }


    private void AbrirVistaDetalladaLaboratorio(LaboratorioDTO labSeleccionado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViewDetalladaLabSeleccionado.fxml"));
            Parent root = loader.load();
            ViewDetalladaLabSeleccionadoController controller = loader.getController();
            controller.setLab(labSeleccionado);
            Stage stage = new Stage();
            stage.setTitle("Detalle del laboratorio");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    void AbrirFormularioEditarLaboratorio(LaboratorioDTO LaboratorioSeleccionado) {
        try {
            LaboratorioDTO completo = AppContext.laboratorio().buscarLaboratorioPorId(LaboratorioSeleccionado.id_lab(),AppContext.getUsuarioActual());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioEditarLaboratorio.fxml"));
            Parent root = loader.load();
            FormularioEditarLaboratorioController controller = loader.getController();
            controller.setLaboratorio(completo);

            Stage stage = new Stage();
            stage.setTitle("Editar laboratorio");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal hasta cerrar
            stage.showAndWait();
            ActualizarTablaLaboratorio();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configurarColumnaAccion() {
        AccionTablaLaboratorio.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = crearBotonAccion("Ver");
            private final Button btnEditar = crearBotonAccion("Editar");
            private final Button btnDeshabilitar = crearBotonAccion("Deshabilitar");
            private final Button btnHabilitar = crearBotonAccion("Habilitar");
            private final HBox box = new HBox(6);

            {
                box.setAlignment(Pos.CENTER_LEFT);
                box.setPadding(new Insets(0, 5, 0, 0));

                btnVer.setOnAction(e -> {
                    LaboratorioDTO lab = getTableView().getItems().get(getIndex());
                    AbrirVistaDetalladaLaboratorio(lab);
                });

                btnEditar.setOnAction(e -> {
                    LaboratorioDTO lab = getTableView().getItems().get(getIndex());
                    AbrirFormularioEditarLaboratorio(lab);
                });

                btnDeshabilitar.setOnAction(e -> {
                    LaboratorioDTO lab = getTableView().getItems().get(getIndex());
                    LaboratorioUpdateDTO dto = crearUpdateDTO(lab);
                    AppContext.laboratorio().deshabilitarLaboratorio(dto, AppContext.getUsuarioActual());
                    getTableView().refresh();
                    ActualizarTablaLaboratorio();
                });

                btnHabilitar.setOnAction(e -> {
                    LaboratorioDTO lab = getTableView().getItems().get(getIndex());
                    LaboratorioUpdateDTO dto = crearUpdateDTO(lab);
                    AppContext.laboratorio().habilitarLaboratorio(dto, AppContext.getUsuarioActual());
                    getTableView().refresh();
                    ActualizarTablaLaboratorio();
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
                LaboratorioDTO lab = getTableView().getItems().get(getIndex());

                if (esAdmin) {
                    if ("habilitado".equalsIgnoreCase(lab.estado_lab())) {
                        box.getChildren().setAll(btnVer, btnEditar, btnDeshabilitar);
                    } else {
                        box.getChildren().setAll(btnVer, btnEditar, btnHabilitar);
                    }
                } else {
                    // MONITOR: solo puede ver
                    box.getChildren().setAll(btnVer);
                }
                setGraphic(box);
            }
        });
    }

    // helper para que todos los botones compartan estilo
    private Button crearBotonAccion(String texto) {
        Button b = new Button(texto);
        b.getStyleClass().add("botonAccion");
        return b;
    }


    private void configurarColumnaEstado() {
        EstadoTablaLaboratorio.setCellFactory(col -> new TableCell<LaboratorioDTO, String>() {
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
                label.getStyleClass().remove("habilitado");
                label.getStyleClass().remove("deshabilitado");

                if ("habilitado".equalsIgnoreCase(item)) {
                    label.getStyleClass().add("habilitado");
                    label.setText("habilitado");
                } else {
                    label.getStyleClass().add("deshabilitado");
                    label.setText("deshabilitado");
                }
                setAlignment(Pos.CENTER);
                setGraphic(label);
                setText(null);
            }
        });
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
                        LimpiarTablaLaboratorio();
                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratoriosPorEstado(busqueda,AppContext.getUsuarioActual()));
                        break;
                    case "nombre":
                        LimpiarTablaLaboratorio();
                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratorioPorNombre(busqueda,AppContext.getUsuarioActual()));
                        break;
                    case "ID":
                        Integer idLaboratorioBuscar = null;
                        if (busqueda != null && !busqueda.isBlank()) {
                            idLaboratorioBuscar = Integer.valueOf(busqueda);
                        }
                        LimpiarTablaLaboratorio();
                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratorioPorId(idLaboratorioBuscar,AppContext.getUsuarioActual()));
                        break;
                    case "ubicacion":
                        LimpiarTablaLaboratorio();
                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratoriosPorUbicacion(busqueda,AppContext.getUsuarioActual()));
                        break;
                    case "capacidadPersonas":
                        LimpiarTablaLaboratorio();
                        Integer capacidadPersonas = Integer.valueOf(busqueda);
                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratoriosPorCapacidades(capacidadPersonas,null, AppContext.getUsuarioActual()));
                        break;
                    case "capacidadEquipos":
                        LimpiarTablaLaboratorio();
                        Integer capacidadEquipos = Integer.valueOf(busqueda);
                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratoriosPorCapacidades(null, capacidadEquipos, AppContext.getUsuarioActual()));
                        break;
            }

        } catch (RuntimeException ex) {// por validaciones de UsuarioController
            ActualizarTablaLaboratorio();
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
    String FiltroNombre(ActionEvent event) {
        FiltroSeleccionado = "nombre";
        Buscar.setText("Buscar por Nombre");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroUbicacion(ActionEvent event) {
        FiltroSeleccionado = "ubicacion";
        Buscar.setText("Buscar por Ubicación");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroCapacidadPersonas(ActionEvent event) {
        FiltroSeleccionado = "capacidadPersonas";
        Buscar.setText("Buscar por Capacidad de Personas");
        return FiltroSeleccionado;
    }

    @FXML
    String FiltroCapacidadEquipos(ActionEvent event) {
        FiltroSeleccionado = "capacidadEquipos";
        Buscar.setText("Buscar por Capacidad de Equipos");
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

    private LaboratorioUpdateDTO crearUpdateDTO(LaboratorioDTO lab) {
        int id = lab.id_lab();
        String nombre_lab = lab.nombre_lab();
        String ubicacion = lab.ubicacion();
        Integer capacidad_personas= lab.capacidad_personas();
        Integer capacidad_equipos = lab.capacidad_equipo();
        String estado_lab = lab.estado_lab();
        return new LaboratorioUpdateDTO(id,nombre_lab,ubicacion,capacidad_personas,capacidad_equipos,estado_lab);
    }

}



