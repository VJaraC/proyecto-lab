package org.example;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        int ID = 14;
        String nombre = "carlos";
        String estado = "activo";
        String password = "1234";

        //crear un objeto con datos (totalmente aislado de la logica de la base de datos)
        Usuario user = new Usuario(ID, nombre, estado, password);

        Conexion conn = new Conexion();




        //ingresar el objeto creado anteriormente a DAO, para poder ingresarlo a la BD
        UsuarioDAO dao = new UsuarioDAO(conn);
//        dao.insertarUsuario(user);
//        dao.mostrarUsuarios();
         Usuario u= dao.buscarUsuario(14, "carlos");
         System.out.println(u.getNombre());

    }
}
