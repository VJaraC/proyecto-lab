//package proyecto.lab.client.controllers;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.HBox;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import proyecto.lab.client.application.AppContext;
//import proyecto.lab.server.dto.LaboratorioDTO;
//import proyecto.lab.server.dto.LaboratorioUpdateDTO;
//
//import java.io.IOException;
//
//public class ViewLaboratoriosController {
//
//    @FXML
//    void AbrirFormularioRegistrarLaboratorio(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioRegistrarLaboratorio.fxml"));
//            Parent root = loader.load();
//
//            Stage stage = new Stage();
//            stage.setTitle("Registrar un Laboratorio");
//            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal hasta cerrar
//            stage.showAndWait();
//            ActualizarTablaLaboratorio();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @FXML
//    private SplitMenuButton Buscar;
//
//    private String FiltroSeleccionado;
//
//    @FXML
//    private TableColumn<LaboratorioDTO, Void> AccionTablaLaboratorio;
//
//    @FXML
//    private TableColumn<LaboratorioDTO, String> CapacidadEquiposTablaLaboratorio;
//
//    @FXML
//    private TableColumn<LaboratorioDTO, String> EstadoTablaLaboratorio;
//
//    @FXML
//    private MenuItem FiltroCapacidadEquipos;
//
//    @FXML
//    private MenuItem FiltroCapacidadPersonas;
//
//    @FXML
//    private MenuItem FiltroEstado;
//
//    @FXML
//    private MenuItem FiltroID;
//
//    @FXML
//    private MenuItem FiltroNombre;
//
//    @FXML
//    private MenuItem FiltroUbicacion;
//
//    @FXML
//    private TableColumn<LaboratorioDTO, Integer> IdTablaLaboratorio;
//
//    @FXML
//    private TableColumn<LaboratorioDTO, String> NombreTablaLaboratorio;
//
//    @FXML
//    private TableView<LaboratorioDTO> TablaLaboratorio;
//
//    @FXML
//    private TableColumn<LaboratorioDTO, String> UbicacionTablaLaboratorio;
//
//    @FXML
//    private Button btnCerrarSesion;
//
//    @FXML
//    private TextField txtBuscar;
//
//    @FXML
//    private Label txtUsuarioSesion;
//
//
//
//
//    private void ActualizarTablaLaboratorio(){
//        TablaLaboratorio.getItems().clear();
//        //TablaLaboratorio.getItems().addAll(AppContext.laboratorio().listaLab());
//        TablaLaboratorio.refresh();
//    }
//
//    private void LimpiarTablaLaboratorio(){
//        TablaLaboratorio.getItems().clear();
//        TablaLaboratorio.refresh();
//    }
//
//
//    @FXML
//    void initialize(){
//        IdTablaLaboratorio.setCellValueFactory(new PropertyValueFactory<>("id"));
//        NombreTablaLaboratorio.setCellValueFactory(new PropertyValueFactory<>("nombre"));
//        UbicacionTablaLaboratorio.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
//        CapacidadEquiposTablaLaboratorio.setCellValueFactory(new PropertyValueFactory<>("capacidadEquipos"));
//        EstadoTablaLaboratorio.setCellValueFactory(new PropertyValueFactory<>("estado"));
//        configurarColumnaAccion();
//        ActualizarTablaLaboratorio();
//        txtUsuarioSesion.setText((AppContext.getUsuarioActual().getNombres()));
//
//        for (MenuItem item : Buscar.getItems()) {
//            item.addEventHandler(ActionEvent.ACTION, e -> {
//                Buscar.setText(item.getText());
//                if (!Buscar.getStyleClass().contains("activo")) {
//                    Buscar.getStyleClass().add("activo");
//                }
//                e.consume(); // evita que el evento se propague a otra acción del SplitMenuButton
//            });
//        }}
//
//
//    void AbrirFormularioEditarLaboratorio(LaboratorioDTO LaboratorioSeleccionado) {
//        try {
//            LaboratorioDTO completo = AppContext.laboratorio().buscarLaboratorioPorId(LaboratorioSeleccionado.id_Laboratorio());
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioEditarLaboratorio.fxml"));
//            Parent root = loader.load();
//            FormularioEditarLaboratorioController controller = loader.getController();
//            controller.setLaboratorio(completo);
//
//            Stage stage = new Stage();
//            stage.setTitle("Editar laboratorio");
//            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal hasta cerrar
//            stage.showAndWait();
//            ActualizarTablaLaboratorio();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void configurarColumnaAccion() {
//        AccionTablaLaboratorio.setCellFactory(col -> {
//            return new TableCell<LaboratorioDTO, Void>() {
//                private final Button btn = new Button("Editar");
//                private final HBox box = new HBox(6, btn);
//
//                {
//                    btn.setOnAction(event -> {
//                        LaboratorioDTO Laboratorio = getTableView().getItems().get(getIndex());
//                        AbrirFormularioEditarLaboratorio(Laboratorio);
//                    });
//                }
//
//                @Override
//                protected void updateItem(Void item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty || getIndex() < 0) {
//                        setGraphic(null);
//                        return;
//                    }
//                    LaboratorioDTO Laboratorio = getTableView().getItems().get(getIndex());
//                    setGraphic(box);
//                }
//            };
//        });
//
//
//    }
//
//    @FXML
//    void Buscar(ActionEvent event) {
//        String busqueda = txtBuscar.getText();
//
//        Buscar.getStyleClass().remove("activo");
//        try{
//
//            if (FiltroSeleccionado == null) {
//                alert(Alert.AlertType.WARNING, "Por favor, selecciona un filtro antes de buscar.");
//                return;
//            }
//            switch (FiltroSeleccionado) {
//                    case "estado":
//                        LimpiarTablaLaboratorio();
//                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratorioPorEstado(busqueda));
//                        break;
//                    case "nombre":
//                        LimpiarTablaLaboratorio();
//                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratorioPorNombre(busqueda));
//                        break;
//                    case "ID":
//                        Integer idLaboratorio = null;
//                        if (busqueda != null && !busqueda.isBlank()) {
//                            idLaboratorio = Integer.valueOf(busqueda);
//                        }
//                        LimpiarTablaLaboratorio();
//                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratorioPorId(idLaboratorio));
//                        break;
//                    case "ubicacion":
//                        LimpiarTablaLaboratorio();
//                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratorioPorUbicacion(busqueda));
//                        break;
//                    case "capacidadPersonas":
//                        LimpiarTablaLaboratorio();
//                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratorioPorCapacidadPersonas(busqueda));
//                        break;
//                    case "capacidadEquipos":
//                        LimpiarTablaLaboratorio();
//                        TablaLaboratorio.getItems().addAll(AppContext.laboratorio().buscarLaboratorioPorCapacidadEquipos(busqueda));
//                        break;
//            }
//
//        } catch (RuntimeException ex) {// por validaciones de UsuarioController
//            ActualizarTablaLaboratorio();
//            alert(Alert.AlertType.ERROR, ex.getMessage());
//        } catch (Exception ex) {
//            alert(Alert.AlertType.ERROR, ex.getMessage());
//        }
//
//    }
//
//    @FXML
//    String FiltroEstado(ActionEvent event) {
//        FiltroSeleccionado = "estado";
//        Buscar.setText("Buscar por Estado");
//        return FiltroSeleccionado;
//    }
//
//    @FXML
//    String FiltroID(ActionEvent event) {
//        FiltroSeleccionado = "ID";
//        Buscar.setText("Buscar por ID");
//        return FiltroSeleccionado;
//    }
//
//    @FXML
//    String FiltroNombre(ActionEvent event) {
//        FiltroSeleccionado = "nombre";
//        Buscar.setText("Buscar por Nombre");
//        return FiltroSeleccionado;
//    }
//
//    @FXML
//    String FiltroUbicacion(ActionEvent event) {
//        FiltroSeleccionado = "ubicacion";
//        Buscar.setText("Buscar por Ubicación");
//        return FiltroSeleccionado;
//    }
//
//    @FXML
//    String FiltroCapacidadPersonas(ActionEvent event) {
//        FiltroSeleccionado = "capacidadPersonas";
//        Buscar.setText("Buscar por Capacidad de Personas");
//        return FiltroSeleccionado;
//    }
//
//    @FXML
//    String FiltroCapacidadEquipos(ActionEvent event) {
//        FiltroSeleccionado = "capacidadEquipos";
//        Buscar.setText("Buscar por Capacidad de Equipos");
//        return FiltroSeleccionado;
//    }
//
//
//    @FXML
//    void txtBuscar(ActionEvent event) {
//
//    }
//
//
//    @FXML
//    void btnCerrarSesion(ActionEvent event) {
//            AppContext.LimpiarSesion();
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/IniciarSesion.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//
//            // 4. Reemplazar la escena actual con la de Login
//            stage.setScene(new Scene(root));
//            stage.setTitle("Sistema de Monitoreo - UNAP");
//            stage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void alert(Alert.AlertType type, String msg) {
//        new Alert(type, msg).showAndWait();
//    }
//
//}
//
//
//
