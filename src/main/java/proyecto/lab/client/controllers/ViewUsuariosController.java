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
import javafx.util.Callback;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioUpdateDTO;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.models.Usuario;
import proyecto.lab.client.application.AppContext;

import javax.swing.plaf.basic.BasicButtonUI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewUsuariosController {

    @FXML
    void AbrirFormularioCrearUsuario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioCrearUsuario.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Crear nuevo usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal hasta cerrar
            stage.showAndWait();
            ActualizarTablaEstudiantes();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TableColumn<UsuarioDTO, Void> AccionTablaEstudiantes;

    @FXML
    private SplitMenuButton Buscar;

    private String FiltroSeleccionado;

    @FXML private Button BotonCrearUsuario;

    @FXML
    private TableColumn<UsuarioDTO, String> ApellidosTablaEstudiantes;

    @FXML
    private MenuItem FiltroEstado;

    @FXML
    private MenuItem FiltroRUT;

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
    private TableColumn<UsuarioDTO, String> EstadoTablaEstudiantes;

    @FXML
    private TableColumn<UsuarioDTO, Integer> IdTablaEstudiantes;

    @FXML
    private TableColumn<UsuarioDTO, String> NombreTablaEstudiantes;

    @FXML
    private TableColumn<UsuarioDTO, String> RUTTablaEstudiantes;

    @FXML
    private TableView<UsuarioDTO> TablaEstudiantes;



    private void ActualizarTablaEstudiantes(){
        TablaEstudiantes.getItems().clear();
        TablaEstudiantes.getItems().addAll(AppContext.admin().listarUsuarios(AppContext.getUsuarioActual()));
        TablaEstudiantes.refresh();
    }

    private void LimpiarTablaEstudiantes(){
        TablaEstudiantes.getItems().clear();
        TablaEstudiantes.refresh();
    }


    @FXML
    void initialize(){
        IdTablaEstudiantes.setCellValueFactory(new PropertyValueFactory<>("ID"));
        EstadoTablaEstudiantes.setCellValueFactory(new PropertyValueFactory<>("estado"));
        NombreTablaEstudiantes.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        RUTTablaEstudiantes.setCellValueFactory(new PropertyValueFactory<>("rut"));
        ApellidosTablaEstudiantes.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        configurarColumnaAccion();
        ActualizarTablaEstudiantes();
        txtUsuarioSesion.setText((AppContext.getUsuarioActual().getNombres()));

        boolean esAdmin = AppContext.getUsuarioActual().getRol() == Rol.ADMIN;
        if (!esAdmin && BotonCrearUsuario != null) {
            BotonCrearUsuario.setVisible(false);
            BotonCrearUsuario.setManaged(false); // colapsa el espacio
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

        TablaEstudiantes.setRowFactory(tv -> {
            TableRow<UsuarioDTO> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && !row.isEmpty()) {
                    AbrirVistaDetalladaUsuario(row.getItem());
                }
            });
            return row;
        });
    }

    private void EditarUsuarios(ActionEvent event){
    }


    void AbrirFormularioEditarUsuario(UsuarioDTO usuarioSeleccionado) {
        try {
            UsuarioDTO completo = AppContext.admin()
                    .buscarUsuarioPorId(usuarioSeleccionado.getID(), AppContext.getUsuarioActual());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioEditarUsuario.fxml"));
            Parent root = loader.load();
            FormularioEditarUsuarioController controller = loader.getController();
            controller.setUsuario(completo);

            Stage stage = new Stage();
            stage.setTitle("Editar usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal hasta cerrar
            stage.showAndWait();
            ActualizarTablaEstudiantes();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configurarColumnaAccion() {
        AccionTablaEstudiantes.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEditar = new Button("Editar");
            private final Button btnDeshabilitar = new Button("Deshabilitar");
            private final Button btnHabilitar = new Button("Habilitar");
            private final HBox box = new HBox(6);

            {
                btnVer.setOnAction(e -> {
                    UsuarioDTO u = getTableView().getItems().get(getIndex());
                    AbrirVistaDetalladaUsuario(u);
                });
                btnEditar.setOnAction(e -> {
                    UsuarioDTO u = getTableView().getItems().get(getIndex());
                    AbrirFormularioEditarUsuario(u);
                });
                btnDeshabilitar.setOnAction(e -> {
                    UsuarioDTO u = getTableView().getItems().get(getIndex());
                    UsuarioUpdateDTO dto = crearUpdateDTO(u);
                    AppContext.admin().deshabilitarUsuario(dto, AppContext.getUsuarioActual());
                    getTableView().refresh();
                });
                btnHabilitar.setOnAction(e -> {
                    UsuarioDTO u = getTableView().getItems().get(getIndex());
                    UsuarioUpdateDTO dto = crearUpdateDTO(u);
                    AppContext.admin().habilitarUsuario(dto, AppContext.getUsuarioActual());
                    getTableView().refresh();
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
                UsuarioDTO usuario = getTableView().getItems().get(getIndex());

                if (!esAdmin) {
                    // MONITOR: solo puede ver
                    box.getChildren().setAll(btnVer);
                } else {
                    // ADMIN: ver + editar + habilitar/deshabilitar
                    if ("habilitado".equalsIgnoreCase(usuario.getEstado())) {
                        box.getChildren().setAll(btnVer, btnEditar, btnDeshabilitar);
                    } else {
                        box.getChildren().setAll(btnVer, btnEditar, btnHabilitar);
                    }
                }
                setGraphic(box);
            }
        });
    }

    private UsuarioUpdateDTO crearUpdateDTO(UsuarioDTO usuario) {
        int id = usuario.getID();
        String nombres = usuario.getNombres();
        String estado =  usuario.getEstado();
        return new UsuarioUpdateDTO(id, nombres, null, usuario.getEstado(), null, null, null, null);
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
                        LimpiarTablaEstudiantes();
                        TablaEstudiantes.getItems().addAll(AppContext.admin().buscarUsuarioPorEstado(busqueda,AppContext.getUsuarioActual()));
                        break;
                    case "nombre":
                        LimpiarTablaEstudiantes();
                        TablaEstudiantes.getItems().addAll(AppContext.admin().buscarUsuarioPorNombre(busqueda,AppContext.getUsuarioActual()));
                        break;
                    case "ID":

                        Integer id = null;
                        if (busqueda != null && !busqueda.isBlank()) {
                            id = Integer.valueOf(busqueda);
                        }
                        LimpiarTablaEstudiantes();
                        TablaEstudiantes.getItems().addAll(AppContext.admin().buscarUsuarioPorId(id,AppContext.getUsuarioActual()));
                        break;
                    case "rut":
                        LimpiarTablaEstudiantes();
                        TablaEstudiantes.getItems().addAll(AppContext.admin().buscarUsuarioPorRUT(busqueda,AppContext.getUsuarioActual()));
                        break;

            }

        } catch (RuntimeException ex) {// por validaciones de UsuarioController
            ActualizarTablaEstudiantes();
            alert(Alert.AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
            alert(Alert.AlertType.ERROR, ex.getMessage());
        }

    }

    private void AbrirVistaDetalladaUsuario(UsuarioDTO usuarioSeleccionado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ViewDetalladaUsuarioSeleccionado.fxml"));
            Parent root = loader.load();
            ViewDetalladaUsuarioSeleccionadoController controller = loader.getController();
            controller.setUsuario(usuarioSeleccionado);
            Stage stage = new Stage();
            stage.setTitle("Detalle del usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
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
        String FiltroRUT(ActionEvent event) {
        FiltroSeleccionado = "rut";
        Buscar.setText("Buscar por RUT");
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



