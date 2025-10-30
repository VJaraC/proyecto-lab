package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;

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
            ActualizarTablaEquipos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private SplitMenuButton Buscar;

    private String FiltroSeleccionado;



    @FXML
    private MenuItem FiltroEstado;

    @FXML
    private MenuItem FiltroID;

    @FXML
    private Label txtUsuarioSesion;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private MenuItem FiltroNombre;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableColumn<EquipoDTO, String> EstadoTablaEquipo;

    @FXML
    private TableColumn<EquipoDTO, Integer> IdTablaEquipo;

    @FXML
    private TableColumn<EquipoDTO, String> NumSerieTablaEquipo;

    @FXML
    private TableColumn<EquipoDTO, Integer> LabTablaEquipo;

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
        IdTablaEquipo.setCellValueFactory(new PropertyValueFactory<>("id"));
        NumSerieTablaEquipo.setCellValueFactory(new PropertyValueFactory<>("estado"));
        LabTablaEquipo.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        ModeloTablaEquipo.setCellValueFactory(new PropertyValueFactory<>("rut"));
        EstadoTablaEquipo.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        configurarColumnaAccion();
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
        }}

    private void EditarUsuarios(ActionEvent event){
    }


    void AbrirFormularioEditarUsuario(EquipoDTO usuarioSeleccionado) {
        try {
            EquipoDTO completo = AppContext.admin().buscarUsuarioPorId(usuarioSeleccionado.getID(), AppContext.getUsuarioActual());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioEditarEquipo.fxml"));
            Parent root = loader.load();
            FormularioEditarUsuarioController controller = loader.getController();
            controller.setEquipo(completo);

            Stage stage = new Stage();
            stage.setTitle("Editar usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal hasta cerrar
            stage.showAndWait();
            ActualizarTablaEquipo();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configurarColumnaAccion() {
        AccionTablaEquipo.setCellFactory(col -> {
            return new TableCell<EquipoDTO, Void>() {
                private final Button btn = new Button("Editar");
                private final Button btnDeshabilitar = new Button("Deshabilitar");
                private final Button btnHabilitar = new Button("Habilitar");
                private final HBox box = new HBox(6, btn);

                {
                    btn.setOnAction(event -> {
                        EquipoDTO usuario = getTableView().getItems().get(getIndex());
                        AbrirFormularioEditarUsuario(usuario);
                    });

                    btnDeshabilitar.setOnAction(event -> {
                        EquipoDTO usuarioDeshabilitar = getTableView().getItems().get(getIndex());
                        EquipoUpdateDTO EquipoUpdateDTO = crearUpdateDTO(usuarioDeshabilitar);
                        AppContext.equipo().deshabilitarEquipo(EquipoUpdateDTO,AppContext.getUsuarioActual());
                        ActualizarTablaEquipo();

                    });

                    btnHabilitar.setOnAction(event -> {
                        EquipoDTO equipo = getTableView().getItems().get(getIndex());
                        EquipoUpdateDTO EquipoUpdateDTO = crearUpdateDTO(equipo);
                        AppContext.equipo().habilitarEquipo(EquipoUpdateDTO,AppContext.getUsuarioActual());
                        ActualizarTablaEquipo();
                    });

                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getIndex() < 0) {
                        setGraphic(null);
                        return;
                    }
                    EquipoDTO equipo = getTableView().getItems().get(getIndex());

                    if ("habilitado".equals(equipo.estado())) {
                        box.getChildren().setAll(btn, btnDeshabilitar);
                    } else {
                        box.getChildren().setAll(btn, btnHabilitar);
                    }
                    setGraphic(box);
                }
            };
        });


    }

    private EquipoUpdateDTO crearUpdateDTO(EquipoDTO equipo) {
        int id = equipo.id_equipo();
        String nombres = equipo.getNombres();
        String estado =  equipo.getEstado();
        return new EquipoUpdateDTO(id, nombres, null, usuario.getEstado(), null, null, null, null, null);
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
                        TablaEquipo.getItems().addAll(AppContext.admin().buscarUsuarioPorEstado(busqueda,AppContext.getUsuarioActual()));
                        break;
                    case "nombre":
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.admin().buscarUsuarioPorNombre(busqueda,AppContext.getUsuarioActual()));
                        break;
                    case "ID":

                        Integer id = null;
                        if (busqueda != null && !busqueda.isBlank()) {
                            id = Integer.valueOf(busqueda);
                        }
                        LimpiarTablaEquipo();
                        TablaEquipo.getItems().addAll(AppContext.admin().buscarUsuarioPorId(id,AppContext.getUsuarioActual()));
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
    String FiltroNombre(ActionEvent event) {
        FiltroSeleccionado = "nombre";
        Buscar.setText("Buscar por Nombre");
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



