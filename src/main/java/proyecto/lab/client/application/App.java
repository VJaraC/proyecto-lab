package proyecto.lab.client.application;
import javafx.application.Application;
import javafx.stage.Stage;
import proyecto.lab.server.controller.AdminController;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.service.UsuarioService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        UsuarioDAO usuarioDao = new UsuarioDAO();         // tu impl real
        UsuarioService usuarioService = new UsuarioService(usuarioDao);
        AdminController admin = new AdminController(usuarioService);

        AppContext.setAdmin(admin);

        Parent root = FXMLLoader.load(getClass().getResource("/views/IniciarSesion.fxml"));
        stage.setTitle("Sistema de Monitoreo - UNAP");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}