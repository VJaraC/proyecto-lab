package org.example;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        //crear un objeto con datos (totalmente aislado de la logica de la base de datos)
        Usuario user = new Usuario(13, "carlos", "1234");

        Conexion conn = new Conexion();

        //ingresar el objeto creado anteriormente a DAO, para poder ingresarlo a la BD
        UsuarioDAO dao = new UsuarioDAO(conn);
        dao.insertarUsuario(user);
    }
}
