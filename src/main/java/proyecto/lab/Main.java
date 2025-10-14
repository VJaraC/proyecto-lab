package proyecto.lab;

import proyecto.lab.server.controller.AdminController;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.UsuarioUpdateDTO;
import proyecto.lab.server.service.UsuarioService;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        try{
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioService usuarioService = new UsuarioService(usuarioDAO);
        AdminController adminController = new AdminController(usuarioService);

//        UsuarioLoginDTO nuevoUsuario = new UsuarioLoginDTO("danilagosh","12345");

//        UsuarioDTO resultado = adminController.crearUsuario(nuevoUsuario);

          UsuarioUpdateDTO usuarioprueba = new UsuarioUpdateDTO(2, "vito" , "habilitado");
          adminController.modificarNombreUsuario(usuarioprueba,"tobisss");
          adminController.habilitarUsuario(usuarioprueba);
        }catch(Exception e){
            System.out.println("Error al crear usuario: " + e.getMessage());
        }

    }
}
