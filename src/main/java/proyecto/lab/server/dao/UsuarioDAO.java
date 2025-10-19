package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//DATA ACCESS OBJECT, se usa para comunicarse directamente con el servidor.
public class UsuarioDAO {
    private final Conexion conexion;

    public UsuarioDAO() {
        this.conexion = new Conexion();
    }


    public Usuario insertarUsuario(Usuario user) { //funcion para insertar un usuario en la base de datos. Se le pasa un objeto del tipo Usuario para ingresar datos.
        String sql = "INSERT INTO usuario(rut , nombre, estado, contrasena) VALUES (?,?,?,?)";
        try (Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, user.getRut());
            ps.setString(2, user.getNombre());
            ps.setString(3, user.getEstado());
            ps.setString(4, user.getContrasena());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario" + e.getMessage());

        }
        return user;
    }

    public boolean actualizarUsuario(Usuario user)  {  //Función para actualizar el nombre de un usuario.
        String sql = "UPDATE usuario SET nombre = ?, estado = ? WHERE id = ?";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, user.getNombre());
            ps.setString(2, user.getEstado());
            ps.setInt(3, user.getID());
            int rows = ps.executeUpdate();

            if (rows > 0) { // Es para actualizar el objeto en memoria y que sea consistente a la BD.
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario" + e.getMessage());
            return false;
        }
        return false;
    }

    public List<Usuario> mostrarUsuarios() { //Función para mostrar los usuarios habilitados en la base de datos.

        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, rut, nombre, estado FROM usuario";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String rut = rs.getString("rut");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                usuarios.add(new Usuario(id, rut, nombre, estado));
            }
            return usuarios;
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios" + e.getMessage());
        }
        return usuarios;
    }

    public Usuario buscarUsuarioPorID(int id) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    int id2 = rs.getInt("id");
                    String rut = rs.getString("rut");
                    String nombre = rs.getString("nombre");
                    String estado = rs.getString("estado");
                    String contrasena = rs.getString("contrasena");

                    usuario = new Usuario(id2 ,rut, nombre, estado, contrasena);
                }
            }
            return null;
        }catch (SQLException e){
            System.out.println("Error al buscar usuario" + e.getMessage());
        }
        return usuario;
    }

    public Usuario buscarUsuarioPorRut(String rut) {
        String sql = "SELECT * FROM usuario WHERE rut = ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("rut"),
                            rs.getString("nombre"),
                            rs.getString("estado"),
                            rs.getString("contrasena")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al buscar usuario por RUT: " + e.getMessage());
        }
    }


    private List<Usuario> mapearUsuarios(ResultSet rs) {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String rut = rs.getString("rut");
                String nombre = rs.getString("nombre");
                String estado = rs.getString("estado");
                String contrasena = rs.getString("contrasena");

                usuarios.add(new Usuario(id, rut, nombre, estado, contrasena));
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al mapear usuarios: + e.getMessage()");
        }
        return usuarios;
    }


    public List<Usuario> buscarUsuarioPorNombre(String n) {
        String sql = "SELECT * FROM usuario WHERE nombre LIKE ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, "%" + n + "%");

            try (ResultSet rs = ps.executeQuery()) {
                return mapearUsuarios(rs);
            }
        }catch (SQLException e) {
            throw AppException.internal("Error al buscar usuario" + e.getMessage());
            }
    }

    public List<Usuario> buscarUsuarioPorEstado(String estado)  {
        String sql = "SELECT * FROM usuario WHERE estado = ?";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {
                return mapearUsuarios(rs);
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al buscar usuario: " + e.getMessage());
        }
    }



    public boolean cambiarEstadoUsuario(Usuario user, String nuevoEstado)  {  //Función para deshabilitar un usuario.
        String sql = "UPDATE usuario SET estado = ? WHERE id = ?";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, nuevoEstado);
            ps.setInt(2, user.getID());
            int rows = ps.executeUpdate();
            if (rows > 0) { // Es para actualizar el objeto en memoria y que sea consistente a la BD.
                user.setEstado(nuevoEstado);
                return true;
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al cambiar estado del usuario" + e.getMessage());
        }
        return false;
    }

}
