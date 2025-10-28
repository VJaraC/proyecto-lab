package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.models.Laboratorio;
import java.sql.*;
import java.time.LocalDate;

import static java.sql.Date.valueOf;

public class LaboratorioDAO {
    private final Conexion conexion;

    public LaboratorioDAO(){
        this.conexion = new Conexion(); }

    public boolean InsertarLaboratorio(Laboratorio laboratorio){
        String sql = "INSERT INTO laboratorio (id_lab, nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab, fecha_registro_lab) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, laboratorio.getId_lab());
            ps.setString(2, laboratorio.getNombre_lab());
            ps.setString(3, laboratorio.getUbicacion());
            ps.setString(4, laboratorio.getCapacidad_personas());
            ps.setString(5, laboratorio.getCapacidad_equipo());
            ps.setString(6, laboratorio.getEstado_lab());
            ps.setDate(7, valueOf(laboratorio.getFecha_registro_lab()));
            int rows = ps.executeUpdate();
            if (rows == 1){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e) {
            System.out.println("Error al insertar laboratorio" + e.getMessage());
            return false;
        }
    }

    public boolean actualizarLaboratorio(Laboratorio laboratorio) {
        String sql = "UPDATE laboratorio SET "
                + "nombre_lab = ?, "
                + "ubicacion = ?, "
                + "capacidad_personas = ?, "
                + "capacidad_equipo = ?, "
                + "estado_lab = ?, "
                + "fecha_registro_lab = ? "
                + "WHERE id_lab = ?";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, laboratorio.getNombre_lab());
            ps.setString(2, laboratorio.getUbicacion());
            ps.setString(3, laboratorio.getCapacidad_personas());
            ps.setString(4, laboratorio.getCapacidad_equipo());
            ps.setString(5, laboratorio.getEstado_lab());
            ps.setDate(7, valueOf(laboratorio.getFecha_registro_lab()));
            ps.setInt(7, laboratorio.getId_lab());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar laboratorio: " + e.getMessage());
            return false;
        }
        return false;
    }

    public Laboratorio BuscarLaboratorioPorId_lab(int id_lab){
        String sql = "SELECT laboratorio WHERE id_lab = ?" ;
        Laboratorio laboratorio = null;

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
                        java.sql.Date fr = rs.getDate("fecha_registro_lab");
                        laboratorio.setFecha_registro_lab(fr.toLocalDate());
                    }
                }
                return laboratorio;
        }catch(SQLException e){
            System.out.println("Error al buscar laboratorio: " + e.getMessage());
        }
        return laboratorio;
    }

    public Laboratorio BuscarLaboratorioPorNombre_lab(String nombre_lab){
        String sql = "SELECT laboratorio WHERE nombre_lab LIKE ?" ;
        Laboratorio laboratorio = null;

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre_lab);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    laboratorio = new Laboratorio();
                    laboratorio.setId_lab(rs.getInt("id_lab"));
                    laboratorio.setNombre_lab(rs.getString("nombre_lab"));
                    laboratorio.setUbicacion(rs.getString("ubicacion"));
                    laboratorio.setCapacidad_personas(rs.getString("capacidad_personas"));
                    laboratorio.setCapacidad_equipo(rs.getString("capacidad_equipo"));
                    laboratorio.setEstado_lab(rs.getString("estado_lab"));
                    java.sql.Date fr = rs.getDate("fecha_registro_lab");
                    laboratorio.setFecha_registro_lab(fr.toLocalDate());
                }
            }
            return laboratorio;
        }catch(SQLException e){
            System.out.println("Error al buscar laboratorio: " + e.getMessage());
        }
        return laboratorio;
    }


}
