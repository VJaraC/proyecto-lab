package proyecto.lab;

import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.models.Usuario;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        //Conexion conn = new Conexion();

        int ID = 14;
        String nombre = "carlos";
        String estado = "activo";
        String password = "1234";

        //crear un objeto con datos (totalmente aislado de la logica de la base de datos)
        Usuario user = new Usuario(ID, nombre, estado, password);

        //ingresar el objeto creado anteriormente a DAO, para poder ingresarlo a la BD
        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.buscarUsuario(2, "vito");
        System.out.println(u.getNombre());
        System.out.println(u.getID());
        System.out.println(u.getPassword());


        if(dao.ActualizarUsuario(u, 1, "coni")){
            /* forma 1  u = dao.buscarUsuario(2, "alonso"); */
            /* forma 2 u.setNombre("coni");*/
            System.out.println(u.getNombre());
        }
    }
}
