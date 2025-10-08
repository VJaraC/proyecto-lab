package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


//DATA ACCESS OBJECT, se usa para comunicarse directamente con el servidor.
public class UsuarioDAO{
    private final Conexion conexion;

    public UsuarioDAO(Conexion c) {
        this.conexion = c;
    }

    public Usuario obtenerUsuarios(){
        String sql = "SELECT * FROM usuarios";
        return null;
    }

    public void insertarUsuario(Usuario user) throws SQLException { //funcion para insertar un usuario en la base de datos. Se le pasa un objeto del tipo Usuario para ingresar datos.
        String sql = "INSERT INTO usuario(ID, nombre, estado, contrasena) VALUES (?,?,?,?)";
        Connection conn = null;
        try{conn = conexion.getTxConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getID());
            ps.setString(2, user.getNombre());
            ps.setString(3, user.getEstado());
            ps.setString(4, user.getPassword());

            conn.commit();
        }
        catch(SQLException e){
            if(conn != null){
                conn.rollback();
            }
            else{
                System.out.println("Error al insertar usuario" + e.getMessage());
            }
        }
    }

}
