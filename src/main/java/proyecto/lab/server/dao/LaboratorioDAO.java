package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Laboratorio;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Date.valueOf;

public class LaboratorioDAO {
    private final Conexion conexion;

    public LaboratorioDAO(){
        this.conexion = new Conexion(); }

    public Laboratorio insertarLaboratorio(Laboratorio laboratorio){
        String sql = "INSERT INTO laboratorio (id_lab, nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab, fecha_registro_lab) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            ps.setString(1, laboratorio.getNombre_lab());
            ps.setString(2, laboratorio.getUbicacion());
            ps.setString(3, laboratorio.getCapacidad_personas());
            ps.setString(4, laboratorio.getCapacidad_equipo());
            ps.setString(5, laboratorio.getEstado_lab());
            ps.setDate(6, valueOf(laboratorio.getFecha_registro_lab()));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()){
                    if (rs.next()) {
                        laboratorio.setId_lab(rs.getInt(1));
                    }
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al insertar laboratorio" + e.getMessage());
        }
        return laboratorio;
    }

    public boolean actualizarLaboratorio(Laboratorio laboratorio) {
        String sql = "UPDATE laboratorio SET nombre_lab = ?, ubicacion = ?, capacidad_equipo = ?, estado_lab = ?, fecha_registro_lab = ? WHERE id_lab = ? ";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, laboratorio.getNombre_lab());
            ps.setString(2, laboratorio.getUbicacion());
            ps.setString(3, laboratorio.getCapacidad_personas());
            ps.setString(4, laboratorio.getCapacidad_equipo());
            ps.setString(5, laboratorio.getEstado_lab());
            ps.setDate(7, Date.valueOf(laboratorio.getFecha_registro_lab()));
            ps.setInt(7, laboratorio.getId_lab());
            int rows = ps.executeUpdate();

            if (rows > 0) { // Para actualizar el objeto en memoria y sea consistente a la BD.
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar laboratorio: " + e.getMessage());
            return false;
        }
        return false;
    }

    public Laboratorio buscarLaboratorioPorIdlab(int id_lab){
        Laboratorio laboratorio = null;
        String sql = "SELECT id_lab, nombre_lab,ubicacion,capacidad_personas,capacidad_equipo,estado_lab,fecha_registro_lab FROM laboratorio  WHERE id_lab = ?";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id_lab);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        laboratorio = new Laboratorio();
                        laboratorio.setId_lab(rs.getInt("id_lab"));
                        laboratorio.setNombre_lab(rs.getString("nombre_lab"));
                        laboratorio.setUbicacion(rs.getString("ubicacion"));
                        laboratorio.setCapacidad_personas(rs.getString("capacidad_personas"));
                        laboratorio.setCapacidad_equipo(rs.getString("capacidad_equipo"));
                        laboratorio.setEstado_lab(rs.getString("estado_lab"));
                        laboratorio.setFecha_registro_lab(rs.getObject("fecha_registro_lab", LocalDate.class));
                    }
                }
                return laboratorio;
        }catch(SQLException e){
            System.err.printf("Error al buscar laboratorio: state=%s code= %d msg= %s%n ", e.getSQLState(), e.getErrorCode(), e.getMessage() );
        }
        return laboratorio;
    }

    public List<Laboratorio> buscarLaboratorioPorUbicacion(String ubicacion){
        String sql = "SELECT id_lab , nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab, fecha_registro_lab FROM laboratorio WHERE ubicacion LIKE ?" ;
        List<Laboratorio> laboratorios = new ArrayList<>();

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, "%" + ubicacion + "%");

            try(ResultSet rs = ps.executeQuery()){
                return mapearLaboratorios(rs);
            }
        } catch (SQLException e){
            System.err.printf("SQL Error al buscar laboratorio por ubicación : state=%s code= %d msg= %s%n", e.getSQLState(), e.getErrorCode(), e.getMessage());
        }
        return laboratorios;
    }

    public List<Laboratorio> buscarLaboratorioPorNombrelab(String nombre_lab){
        String sql = "SELECT id_lab , nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab, fecha_registro_lab FROM laboratorio WHERE nombre_lab ILIKE ?" ;
        List<Laboratorio> laboratorios = new ArrayList<>();

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre_lab + "%");

            try(ResultSet rs = ps.executeQuery()){
                return mapearLaboratorios(rs);
            }
        }catch(SQLException e){
            System.out.println("Error al buscar laboratorio: " + e.getMessage());
        }
        return laboratorios;
    }

    public List<Laboratorio> mostrarLaboratorios(){
        List<Laboratorio> laboratorios = new ArrayList<>();
        String sql = "SELECT * FROM laboratorio";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id_lab = rs.getInt("id_lab");
                String nombre_lab = rs.getString("nombre_lab");
                String ubicacion = rs.getString("ubicacion");
                String capacidad_personas = rs.getString("capacidad_personas");
                String capacidad_equipo = rs.getString("capacidad_equipo");
                String estado_lab = rs.getString("estado_lab");
                LocalDate fecha_registro_lab = rs.getDate("fecha_registro_lab").toLocalDate();
                laboratorios.add(new Laboratorio(id_lab, nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab , fecha_registro_lab));
            }
            return laboratorios;
        } catch (SQLException e) {
            System.out.println("Error al listar laboratorios" + e.getMessage());
        }
        return laboratorios;
    }

    public boolean cambiarEstadoLaboratorio(Laboratorio lab, String nuevoEstado)  {  //Función para deshabilitar un usuario.
        String sql = "UPDATE Laboratorio SET estado_lab = ? WHERE id_lab = ?";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, nuevoEstado);
            ps.setInt(2, lab.getId_lab());
            int rows = ps.executeUpdate();
            if (rows > 0) { // Es para actualizar el objeto en memoria y que sea consistente a la BD.
                lab.setEstado_lab(nuevoEstado);
                return true;
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al cambiar estado del usuario" + e.getMessage());
        }
        return false;
    }


    private List<Laboratorio> mapearLaboratorios(ResultSet rs) {
        List<Laboratorio> laboratorios = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre_lab = rs.getString("nombre_lab");
                String ubicacion = rs.getString("ubicacion");
                String capacidad_personas = rs.getString("capacidad_personas");
                String capacidad_equipo = rs.getString("capacidad_equipo");
                String estado_lab = rs.getString("estado_lab");
                LocalDate fecha_registro_lab = rs.getObject("fecha_registro_lab", LocalDate.class);

                laboratorios.add(new Laboratorio(id, nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab,fecha_registro_lab));
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al mapear laboratorios: + e.getMessage()");
        }
        return laboratorios;
    }


}

