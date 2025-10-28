package proyecto.lab;

import proyecto.lab.server.controller.UsuarioController;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.service.UsuarioService;

import java.sql.SQLException;
import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioService usuarioService = new UsuarioService(usuarioDAO);
        UsuarioController usuarioController = new UsuarioController(usuarioService);

        UsuarioLoginDTO nuevousuario = new UsuarioLoginDTO();
        nuevousuario.setNombre("Vito");
        nuevousuario.setApellidos("Jara");
        nuevousuario.setRut("21243169-9");
        nuevousuario.setContrasena("vito123");
        nuevousuario.setGenero("Masculino");
        nuevousuario.setEmail("nuevousuario@example.com");
        nuevousuario.setCargo("TI");
        nuevousuario.setTelefono("964031692");
        nuevousuario.setFecha_nacimiento(LocalDate.of(2002,5,12));


        UsuarioDTO adminAuth = new UsuarioDTO();
        adminAuth.setID(777);
        adminAuth.setNombre("Administrador");
        adminAuth.setRol(Rol.ADMIN);

        usuarioController.crearUsuario(nuevousuario,adminAuth);


    }
}
