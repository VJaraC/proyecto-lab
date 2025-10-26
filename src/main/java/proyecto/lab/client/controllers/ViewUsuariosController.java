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
        TablaEstudiantes.getItems().addAll(AppContext.admin().listarUsuarios());
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
        configurarColumnaAccion();
        ActualizarTablaEstudiantes();
        txtUsuarioSesion.setText((AppContext.getUsuarioActual().getNombres()));
    }

    private void EditarUsuarios(ActionEvent event){
    }


    void AbrirFormularioEditarUsuario(UsuarioDTO usuarioSeleccionado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FormularioEditarUsuario.fxml"));
            Parent root = loader.load();
            FormularioEditarUsuarioController controller = loader.getController();
            controller.setUsuario(usuarioSeleccionado);

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
        AccionTablaEstudiantes.setCellFactory(col -> {
            return new TableCell<UsuarioDTO, Void>() {
                private final Button btn = new Button("Editar");
                private final Button btnDeshabilitar = new Button("Deshabilitar");
                private final Button btnHabilitar = new Button("Habilitar");
                private final HBox box = new HBox(6, btn);

                {
                    btn.setOnAction(event -> {
                        UsuarioDTO usuario = getTableView().getItems().get(getIndex());
                        AbrirFormularioEditarUsuario(usuario);
                    });

                    btnDeshabilitar.setOnAction(event -> {
                        UsuarioDTO usuarioDeshabilitar = getTableView().getItems().get(getIndex());
                        UsuarioUpdateDTO usuarioUpdateDTO = crearUpdateDTO(usuarioDeshabilitar);
                        AppContext.admin().deshabilitarUsuario(usuarioUpdateDTO);
                        ActualizarTablaEstudiantes();

                    });

                    btnHabilitar.setOnAction(event -> {
                        UsuarioDTO usuario = getTableView().getItems().get(getIndex());
                        UsuarioUpdateDTO usuarioUpdateDTO = crearUpdateDTO(usuario);
                        AppContext.admin().habilitarUsuario(usuarioUpdateDTO);
                        ActualizarTablaEstudiantes();
                    });

                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getIndex() < 0) {
                        setGraphic(null);
                        return;
                    }
                    UsuarioDTO usuario = getTableView().getItems().get(getIndex());

                    if ("habilitado".equals(usuario.getEstado())) {
                        box.getChildren().setAll(btn, btnDeshabilitar);
                    } else {
                        box.getChildren().setAll(btn, btnHabilitar);
                    }
                    setGraphic(box);
                }
            };
        });


    }

    private UsuarioUpdateDTO crearUpdateDTO(UsuarioDTO usuario) {
        UsuarioUpdateDTO usuarioUpdateDTO = new UsuarioUpdateDTO();
        usuarioUpdateDTO.setNombres(usuario.getNombres());
        usuarioUpdateDTO.setEstado(usuario.getEstado());
        usuarioUpdateDTO.setId(usuario.getID());
        return usuarioUpdateDTO;
    }


    @FXML
    void Buscar(ActionEvent event) {
        String busqueda = txtBuscar.getText();
        try{

            if (FiltroSeleccionado == null) {
                alert(Alert.AlertType.WARNING, "Por favor, selecciona un filtro antes de buscar.");
                return;
            }
            switch (FiltroSeleccionado) {
                    case "estado":
                        LimpiarTablaEstudiantes();
                        TablaEstudiantes.getItems().addAll(AppContext.admin().buscarUsuarioPorEstado(busqueda));
                        break;
                    case "nombre":
                        LimpiarTablaEstudiantes();
                        TablaEstudiantes.getItems().addAll(AppContext.admin().buscarUsuarioPorNombre(busqueda));
                        break;
                    case "ID":

                        Integer id = null;
                        if (busqueda != null && !busqueda.isBlank()) {
                            id = Integer.valueOf(busqueda);
                        }
                        LimpiarTablaEstudiantes();
                        TablaEstudiantes.getItems().addAll(AppContext.admin().buscarUsuarioPorId(id));
                        break;
            }

        } catch (RuntimeException ex) {// por validaciones de AdminController
            ActualizarTablaEstudiantes();
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



