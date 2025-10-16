package proyecto.lab.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableColumn<UsuarioDTO, String> EstadoTablaEstudiantes;

    @FXML
    private TableColumn<UsuarioDTO, Integer> IdTablaEstudiantes;

    @FXML
    private TableColumn<UsuarioDTO, String> NombreTablaEstudiantes;

    @FXML
    private TableView<UsuarioDTO> TablaEstudiantes;

    private void ActualizarTablaEstudiantes(){
        TablaEstudiantes.getItems().clear();
        TablaEstudiantes.getItems().addAll(AppContext.admin().listarUsuarios());
        TablaEstudiantes.refresh();
    }

    @FXML
    void initialize(){
        IdTablaEstudiantes.setCellValueFactory(new PropertyValueFactory<>("ID"));
        EstadoTablaEstudiantes.setCellValueFactory(new PropertyValueFactory<>("estado"));
        NombreTablaEstudiantes.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        configurarColumnaAccion();

        ActualizarTablaEstudiantes();
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

                {
                    btn.setOnAction(event -> {
                        UsuarioDTO usuario = getTableView().getItems().get(getIndex());
                        AbrirFormularioEditarUsuario(usuario);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
        });
    }



}



