package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.models.Usuario;
import java.sql.SQLException;
import java.sql.*;


//DATA ACCESS OBJECT, se usa para comunicarse directamente con el servidor.
public class UsuarioDAO {
    private final Conexion conexion;

    public UsuarioDAO() {
        this.conexion = new Conexion();
    }


    public void insertarUsuario(Usuario user) { //funcion para insertar un usuario en la base de datos. Se le pasa un objeto del tipo Usuario para ingresar datos.
        String sql = "INSERT INTO usuario(ID, nombre, estado, contrasena) VALUES (?,?,?,?)";
        try (Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, user.getID());
            ps.setString(2, user.getNombre());
            ps.setString(3, user.getEstado());
            ps.setString(4, user.getContrasena());
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

    public Usuario buscarUsuarioPorID(int id) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    int id2 = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String estado = rs.getString("estado");
                    String contrasena = rs.getString("contrasena");
                    usuario = new Usuario(id2, nombre, estado, contrasena);
                }
            } catch (SQLException e) {
                System.out.println("Error al buscar usuario" + e.getMessage());
            }
        }catch (SQLException e){
            System.out.println("Error al buscar usuario" + e.getMessage());
        }
        return usuario;
    }

    public boolean actualizarNombre(Usuario user, String nombre) throws SQLException {  //Función para actualizar el nombre de un usuario.
        String sql = "UPDATE usuario SET nombre = ? WHERE id = ?";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, nombre);
            ps.setInt(2, user.getID());

            int rows = ps.executeUpdate();

            if (rows > 0) { // Es para actualizar el objeto en memoria y que sea consistente a la BD.
                user.setNombre(nombre);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario" + e.getMessage());
            return false;
        }
        return false;
    }

    public boolean cambiarEstadoUsuario(Usuario user, String nuevoEstado) throws SQLException {  //Función para deshabilitar un usuario.
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
            System.out.println("Error al cambiar estado de usuario: " + e.getMessage());
            return false;
        }
        return false;
    }

}
