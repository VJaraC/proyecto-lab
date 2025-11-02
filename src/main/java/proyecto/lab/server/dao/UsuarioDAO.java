package proyecto.lab.server.dao;
import org.jetbrains.annotations.NotNull;
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
        String sql = "INSERT INTO usuario(rut, nombres, apellidos, email, estado, genero, contrasena, cargo, rol, fecha_nac, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


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
            if(user.getFecha_nac() != null){
                ps.setDate(10, Date.valueOf(user.getFecha_nac()));
            } else{
                ps.setNull(10, Types.DATE);
            }
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
            throw AppException.internal("Error al insertar el usuario: " + e.getMessage());
        }
        return user;
    }


    public boolean actualizarUsuario(Usuario user)  {  //Función para actualizar el nombre de un usuario.
        final String sql = "UPDATE usuario SET nombres = ?, apellidos = ?, email = ?, estado = ?, genero = ?,cargo = ?,  rol= ?, fecha_nac = ? ,telefono = ?  WHERE id = ?";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, user.getNombres());
            ps.setString(2, user.getApellidos());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getEstado());
            ps.setString(5, user.getGenero());
            ps.setString(6, user.getCargo());
            ps.setString(7, user.getRol().name());

            if(user.getFecha_nac() != null){
                ps.setDate(8, Date.valueOf(user.getFecha_nac()));
            } else{
                ps.setNull(8, Types.DATE);
            }

            ps.setString(9, user.getTelefono());

            ps.setInt(10, user.getID());
            int rows = ps.executeUpdate();

            if (rows == 1) { // Es para actualizar el objeto en memoria y que sea consistente a la BD.
                return true;
            }
        } catch (SQLException e) {
             throw AppException.internal("Error al actualizar usuario" + e.getMessage());
        }
        return false;
    }


    public List<Usuario> mostrarUsuarios() { //Función para mostrar los usuarios en la base de datos.

        List<Usuario> usuarios = new ArrayList<>();
        final String sql = "SELECT id, rut, nombres, apellidos, email, estado, genero, cargo, rol , fecha_nac, telefono FROM usuario";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
               usuarios.add(mapRowUsuario(rs));
            }
            return usuarios;
        } catch (SQLException e) {
            throw AppException.internal("Error al mostrar usuarios " + e.getMessage());
        }
    }


    public Usuario buscarUsuarioPorID(int id) {
        final String sql = " SELECT id, rut, nombres, apellidos, email, estado, genero, cargo, rol, fecha_nac, telefono FROM usuario WHERE id = ? ";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowUsuario(rs);
                }
            }
            throw AppException.notFound("Usuario no encontrado.");

        } catch (SQLException e) {
            throw AppException.internal("Error al buscar usuario por ID: " + e.getMessage());
        }
    }

    //implementar mapearUsuarios.
    public Usuario buscarUsuarioPorRut(String rut) {
        final String sql = "SELECT * FROM usuario WHERE rut = ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowUsuario(rs);
                }
            }
            // No hay fila.
            throw AppException.notFound("No se encontró al usuario.");

        } catch (SQLException e) {
            throw AppException.internal("Error al buscar usuario por RUT: " + e.getMessage());
        }
    }

    public List<Usuario> buscarUsuarios(String nombre, String apellidos, String estado, String genero,
                                        String cargo, LocalDate fechaNac, String telefono) {
        List<Usuario> usuarios = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id, rut, nombres, apellidos, email, estado, genero, cargo, rol, fecha_nac, telefono FROM usuario WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            sql.append(" AND nombres ILIKE ?");
            params.add("%" + nombre.trim() + "%");
        }
        if (apellidos != null && !apellidos.isBlank()) {
            sql.append(" AND apellidos ILIKE ?");
            params.add("%" + apellidos.trim() + "%");
        }
        if (estado != null && !estado.isBlank()) {
            sql.append(" AND estado = ?");
            params.add(estado.trim().toLowerCase());
        }
        if (genero != null && !genero.isBlank()) {
            sql.append(" AND genero ILIKE ?");
            params.add("%" + genero.trim() + "%");
        }
        if (cargo != null && !cargo.isBlank()) {
            sql.append(" AND cargo ILIKE ?");
            params.add("%" + cargo.trim() + "%");
        }
        if (telefono != null && !telefono.isBlank()) {
            sql.append(" AND telefono ILIKE ?");
            params.add("%" + telefono.trim() + "%");
        }
        if (fechaNac != null) {
            sql.append(" AND fecha_nac = ?");
            params.add(Date.valueOf(fechaNac));
        }

        sql.append(" ORDER BY apellidos, nombres");

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapRowUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al buscar usuarios: " + e.getMessage());
        }

        return usuarios;
    }



    public boolean cambiarEstadoUsuario(Usuario user, String nuevoEstado)  {  //Función para deshabilitar un usuario.
        String sql = "UPDATE usuario SET estado = ? WHERE id = ?";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, nuevoEstado);
            ps.setInt(2, user.getID());
            int rows = ps.executeUpdate();
            if (rows == 0) { throw AppException.notFound("No se pudo cambiar el estado."); }
            if (rows > 0) { // Es para actualizar el objeto en memoria y que sea consistente a la BD.
                user.setEstado(nuevoEstado);
                return true;
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al cambiar estado del usuario" + e.getMessage());
        }
        return false;
    }


    @NotNull
    private List<Usuario> getUsuarios(String n, String sql) {
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

    //se le pasa un resultset (resultados de una consulta generada) para poder traducirlas (mapearlas) a un objeto usuario el cual será ingresado a una lista para tener facil acceso a los objetos.
    //modificarla.
    private List<Usuario> mapearUsuarios(ResultSet rs) {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            while (rs.next()) {
                usuarios.add(mapRowUsuario(rs));
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al mapear usuarios: "+ e.getMessage());
        }
        return usuarios;
    }

    private static boolean hasColumn(ResultSet rs, String name) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int cols = md.getColumnCount();
        for (int i = 1; i <= cols; i++) {
            // usar getColumnLabel para respetar alias del SELECT
            if (name.equalsIgnoreCase(md.getColumnLabel(i))) {
                return true;
            }
        }
        return false;
    }

    private Usuario mapRowUsuario(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            String rut = rs.getString("rut");
            String nombres = rs.getString("nombres");
            String apellidos = rs.getString("apellidos");
            String email = rs.getString("email");
            String estado = rs.getString("estado");
            String genero = rs.getString("genero");
            String cargo = rs.getString("cargo");
            String telefono = rs.getString("telefono");
            // Fallback seguro para el rol
            Rol rol = Rol.MONITOR;
            String rolStr = rs.getString("rol");
            if (rolStr != null && !rolStr.isBlank()) {
                try {
                    rol = Rol.valueOf(rolStr.trim().toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    rol = Rol.MONITOR; }
            }
            LocalDate fecha_nac = rs.getObject("fecha_nac", LocalDate.class);
            // contrasena es opcional en el SELECT
            String contrasena = null;
            if (hasColumn(rs, "contrasena")) {
                contrasena = rs.getString("contrasena"); // puede seguir siendo null y está bien
            }

            return new Usuario(id, rut, nombres, apellidos, email, estado, genero,
                    contrasena, cargo, rol, fecha_nac, telefono);

        } catch (SQLException e) {
            throw AppException.internal("Error al mapear fila de usuario: " + e.getMessage());
        }
    }




}
