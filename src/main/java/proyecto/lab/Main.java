package proyecto.lab;

import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.models.Usuario;
import proyecto.lab.server.service.UsuarioService;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        //Conexion conn = new Conexion();
        UsuarioService usuarioservice = new UsuarioService();
        Usuario u = new Usuario(1,"lagosh","habilitado","danilago123");

//        usuarioservice.crearUsuario(u);
        usuarioservice.actualizarNombreUsuario(u,"dani");
//        usuarioservice.habilitarUsuario(u);


    }
}
