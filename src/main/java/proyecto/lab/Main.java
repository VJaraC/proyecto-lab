package proyecto.lab;

import proyecto.lab.server.controller.UsuarioController;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.service.UsuarioService;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioService usuarioService = new UsuarioService(usuarioDAO);
        UsuarioController usuarioController = new UsuarioController(usuarioService);

        UsuarioLoginDTO nuevousuario = new UsuarioLoginDTO();
        nuevousuario.setNombre("Vito");
        nuevousuario.setRut("21243169-9");
        nuevousuario.setContrasena("vito123");
        usuarioController.crearUsuario(nuevousuario);

    }
}
