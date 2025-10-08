package org.example;

import java.sql.*;


//DATA ACCESS OBJECT, se usa para comunicarse directamente con el servidor.
public class UsuarioDAO {
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
        try {
            conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getID());
            ps.setString(2, user.getNombre());
            ps.setString(3, user.getEstado());
            ps.setString(4, user.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario" + e.getMessage());

        }
    }

    public void mostrarUsuarios() throws SQLException { //Función para mostrar los usuarios habilitados en la base de datos.
        try (Connection c = conexion.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuario WHERE estado ='habilitado'")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                String contrasena = rs.getString("contrasena");
                System.out.println(id + " " + nombre + " " + estado + " " + contrasena);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar usuario" + e.getMessage());
        }
    }

    public Usuario buscarUsuario(int parametro_id, Object valor) throws SQLException {
        Usuario usuario = null;
        String sql = null;
        switch (parametro_id) { // cuando es 1, se busca por id -- Cuando sea 2, se busca por nombre
            case 1:
                sql = "SELECT * FROM usuario WHERE id = ?";
                break;
            case 2:
                sql = "SELECT * FROM usuario WHERE nombre = ?";
                break;
        }
        try (PreparedStatement ps = conexion.getConnection().prepareStatement(sql)) {
            ps.setObject(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id2 = rs.getInt("id");
                    String nombre2 = rs.getString("nombre");
                    String estado2 = rs.getString("estado");
                    String contrasena2 = rs.getString("contrasena");
                    usuario = new Usuario(id2, nombre2, estado2, contrasena2);
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar usuario" + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario" + e.getMessage());
        }
        return usuario;
    }

    public boolean ActualizarUsuario(Usuario user, int parametro_id, Object valor) throws SQLException {
        String sql = null;
        switch (parametro_id) {
            case 1: //nombre
                sql = "UPDATE usuario SET nombre = ? WHERE id=?";
                break;
            case 2: //contrasena
                sql = "UPDATE usuario SET contrasena = ? WHERE id=?";
                break;
        }
        try (PreparedStatement ps = conexion.getConnection().prepareStatement(sql)) {
            ps.setObject(1, valor);
            ps.setInt(2, user.getID());
            ps.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            System.out.println("Error al actualizar usuario" + e.getMessage());
            return false;
        }
    }
}
