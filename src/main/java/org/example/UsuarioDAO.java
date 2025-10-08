package org.example;

import java.sql.*;


//DATA ACCESS OBJECT, se usa para comunicarse directamente con el servidor.
public class UsuarioDAO{
    private final Conexion conexion;

    public UsuarioDAO(Conexion c) {
        this.conexion = c;
    }

//    public Usuario obtenerUsuarios(){
//        String sql = "SELECT * FROM usuarios";
//        return null;
//    }

    public void insertarUsuario(Usuario user) throws SQLException { //funcion para insertar un usuario en la base de datos. Se le pasa un objeto del tipo Usuario para ingresar datos.
        String sql = "INSERT INTO usuario(ID, nombre, estado, contrasena) VALUES (?,?,?,?)";
        Connection conn = null;
        try{conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getID());
            ps.setString(2, user.getNombre());
            ps.setString(3, user.getEstado());
            ps.setString(4, user.getPassword());

            ps.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("Error al insertar usuario" + e.getMessage());

        }
    }

    public void mostrarUsuarios() throws SQLException{
        try(Connection c = conexion.getConnection();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM usuario WHERE estado ='habilitado'")){
            while(rs.next()){
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                String contrasena = rs.getString("contrasena");
                System.out.println(id+" "+nombre+" "+estado+" "+contrasena);
            }
        }catch(SQLException e){
            System.out.println("Error al mostrar usuario" + e.getMessage());
        }
    }

}
