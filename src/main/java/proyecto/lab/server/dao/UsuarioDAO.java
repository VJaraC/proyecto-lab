package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.models.Usuario;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//DATA ACCESS OBJECT, se usa para comunicarse directamente con el servidor.
public class UsuarioDAO {
    private final Conexion conexion;

    public UsuarioDAO() {
        this.conexion = new Conexion();
    }


    public Usuario insertarUsuario(Usuario user) { //funcion para insertar un usuario en la base de datos. Se le pasa un objeto del tipo Usuario para ingresar datos.
        String sql = "INSERT INTO USUARIO(rut, nombres, apellidos, email, estado, genero, contrasena, cargo, rol, fecha_nac, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getRut());
            ps.setString(2, user.getNombres());
            ps.setString(3, user.getApellidos());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getEstado());
            ps.setString(6, user.getGenero());
            ps.setString(7, user.getContrasena());
            ps.setString(8, user.getCargo());
            ps.setString(9, user.getRol().name());
            ps.setDate(10, Date.valueOf(user.getFecha_nacimiento()));
            ps.setString(11, user.getTelefono());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setID(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario" + e.getMessage());

        }
        return user;
    }


    public boolean actualizarUsuario(Usuario user)  {  //Función para actualizar el nombre de un usuario.
        final String sql = "UPDATE usuario SET nombres = ?, apellidos = ?, estado = ?, rol= ?, email = ?, telefono = ?, contrasena = ?, cargo = ? WHERE id = ?";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, user.getNombres());
            ps.setString(2, user.getApellidos());
            ps.setString(3, user.getEstado());
            ps.setString(4, user.getRol().name());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getTelefono());
            ps.setString(7, user.getContrasena());
            ps.setString(8, user.getCargo());
            ps.setInt(9, user.getID());
            int rows = ps.executeUpdate();

            if (rows > 0) { // Es para actualizar el objeto en memoria y que sea consistente a la BD.
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar datos del usuario" + e.getMessage());
            return false;
        }
        return false;
    }


    public List<Usuario> mostrarUsuarios() { //Función para mostrar los usuarios habilitados en la base de datos.

        List<Usuario> usuarios = new ArrayList<>();
        final String sql = "SELECT id, rut, nombres, apellidos, estado, cargo , rol FROM usuario ";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String rut = rs.getString("rut");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String email = rs.getString("email");
                String estado = rs.getString("estado");
                String genero = rs.getString("genero");
                String cargo = rs.getString("cargo");
                LocalDate fechaNacimiento = rs.getObject("fecha_nacimiento", LocalDate.class);
                String telefono = rs.getString("telefono");
                String rolStr = rs.getString("rol");

                Rol rol = Rol.valueOf(rolStr.toUpperCase());
                usuarios.add(new Usuario(id, rut, nombres, apellidos, email,estado, genero, cargo , rol , fechaNacimiento, telefono));
            }
            return usuarios;
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios" + e.getMessage());
        }
        return usuarios;
    }


    public Usuario buscarUsuarioPorID(int id) {
        final String sql = "SELECT id, rut, nombres, apellidos, email, estado, genero, cargo, rol , fecha_nac, telefono FROM usuario WHERE id = ?";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    // No se encontró el usuario
                    throw AppException.notFound("Usuario no encontrado");
                }

                // Parsear el rol con fallback a MONITOR
                Rol rol;
                String rolStr = rs.getString("rol");
                if (rolStr == null || rolStr.isBlank()) {
                    rol = Rol.MONITOR;
                } else {
                    try {
                        rol = Rol.valueOf(rolStr.trim().toUpperCase());
                    } catch (IllegalArgumentException ex) {
                        rol = Rol.MONITOR;
                    }
                }

                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("rut"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("estado"),
                        rs.getString("genero"),
                        rs.getString("cargo"),
                        rol,
                        rs.getObject("fecha_nac", java.time.LocalDate.class),
                        rs.getString("telefono")
                );
            }

        } catch (SQLException e) {
            System.err.printf(
                    "SQL Error al buscar usuario por ID (id=%d): state=%s code=%d msg=%s%n",
                    id, e.getSQLState(), e.getErrorCode(), e.getMessage()
            );
            // Si ocurre error de SQL, lanzamos una excepción interna de tu sistema
            throw AppException.internal("Error al buscar usuario en la base de datos");
        }
    }



    //implementar mapearUsuarios.
    public Usuario buscarUsuarioPorRut(String rut) {
        final String sql = "SELECT id, rut, nombres, apellidos, email , estado, genero , contrasena, cargo, rol, fecha_nac, telefono FROM usuario WHERE rut = ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.sql.Date sqlDate = rs.getDate("fecha_nac");
                    java.time.LocalDate fechaNacimiento = (sqlDate != null ? sqlDate.toLocalDate() : null);

                    String rolStr = rs.getString("rol");
                    Rol rol = null;
                    if(rolStr != null && !rolStr.isBlank()){
                        try{
                            rol = Rol.valueOf(rolStr.toUpperCase());
                        } catch (IllegalArgumentException e){
                            rol = Rol.MONITOR;
                        }
                    } else{
                        rol = Rol.MONITOR;
                    }
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("rut"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("estado"),
                            rs.getString("genero"),
                            rs.getString("contrasena"),
                            rs.getString("cargo"),
                            rol,
                            fechaNacimiento,
                            rs.getString("telefono")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al buscar usuario por RUT: " + e.getMessage());
        }
    }


    //se le pasa un resultset (resultados de una consulta generada) para poder traducirlas (mapearlas) a un objeto usuario el cual será ingresado a una lista para tener facil acceso a los objetos.
    //modificarla.
    private List<Usuario> mapearUsuarios(ResultSet rs) {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String rut = rs.getString("rut");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String email = rs.getString("email");
                String estado = rs.getString("estado");
                String genero = rs.getString("genero");
                String contrasena = rs.getString("contrasena");
                String cargo = rs.getString("cargo");

                String rolStr = rs.getString("rol");
                Rol rol = Rol.MONITOR;
                if(rolStr != null && !rolStr.isBlank()){
                    try{
                        rol = Rol.valueOf(rolStr.toUpperCase());
                    } catch (IllegalArgumentException ignored){

                    }
                }
                LocalDate  fechaNacimiento = rs.getObject("fecha_nac", LocalDate.class);
                String telefono = rs.getString("telefono");

                usuarios.add(new Usuario(id, rut, nombres, apellidos, email, estado, genero, contrasena, cargo, rol, fechaNacimiento, telefono));
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al mapear usuarios: + e.getMessage()");
        }
        return usuarios;
    }


    public List<Usuario> buscarUsuarioPorNombre(String n) {
        String sql = "SELECT id, rut, nombres, apellidos,email,estado,genero,cargo,fecha_nac,telefono,rol FROM usuario WHERE nombres ILIKE ? or apellidos ILIKE ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, "%" + n + "%");
            ps.setString(2, "%" + n + "%");

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
