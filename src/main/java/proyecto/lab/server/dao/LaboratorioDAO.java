package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Laboratorio;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LaboratorioDAO {
    private final Conexion conexion;

    public LaboratorioDAO(){
        this.conexion = new Conexion(); }

    public Laboratorio insertarLaboratorio(Laboratorio lab) { //funcion para insertar un laboratorio en la base de datos. Se le pasa un objeto del tipo laboratorio para ingresar datos.
        final String sql = """
    INSERT INTO laboratorio (nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab, fecha_registro_lab)
    VALUES (?, ?, ?, ?, ?, ?)
    RETURNING id_lab
""";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lab.getNombre_lab());
            ps.setString(2, lab.getUbicacion());
            ps.setInt(3, lab.getCapacidad_personas());
            ps.setInt(4, lab.getCapacidad_equipo());
            ps.setString(5, lab.getEstado_lab());

            if(lab.getFecha_registro_lab() != null){
                ps.setDate(6, Date.valueOf(lab.getFecha_registro_lab()));
            } else{
                ps.setNull(6, Types.DATE);
            }

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        lab.setId_lab(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al insertar el laboratorio: " + e.getMessage());
        }
        return lab;
    }


    public boolean actualizarLaboratorio(Laboratorio lab)  {  //Función para actualizar el laboratorio.
        final String sql = "UPDATE laboratorio SET nombre_lab = ?, ubicacion = ?, capacidad_personas = ?, capacidad_equipo = ?," +
                "estado_lab = ?,  fecha_registro_lab= ?  WHERE id_lab = ?";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, lab.getNombre_lab());
            ps.setString(2, lab.getUbicacion());
            ps.setInt(3, lab.getCapacidad_personas());
            ps.setInt(4, lab.getCapacidad_equipo());
            ps.setString(5, lab.getEstado_lab());

            if(lab.getFecha_registro_lab() != null){
                ps.setDate(6, Date.valueOf(lab.getFecha_registro_lab()));
            } else{
                ps.setNull(6, Types.DATE);
            }
            ps.setInt(7, lab.getId_lab());

            int rows = ps.executeUpdate();

            if (rows == 1) { // Es para actualizar el objeto en memoria y que sea consistente a la BD.
                return true;
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al actualizar el laboratorio" + e.getMessage());
        }
        return false;
    }


    public List<Laboratorio> mostrarLaboratorios() { //Función para mostrar los laboratorios en la base de datos.

        List<Laboratorio> laboratorios = new ArrayList<>();
        final String sql = "SELECT id_lab, nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab , fecha_registro_lab FROM laboratorio";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                laboratorios.add(mapRowLaboratorio(rs));
            }
            return laboratorios;
        } catch (SQLException e) {
            throw AppException.internal("Error al mostrar laboratorios " + e.getMessage());
        }
    }


    public Laboratorio buscarLaboratorioPorID(int id_lab) {
        final String sql = " SELECT id_lab, nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab, fecha_registro_lab FROM laboratorio WHERE id_lab = ? ";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id_lab);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowLaboratorio(rs);
                }
            }
            throw AppException.notFound("Laboratorio no encontrado.");

        } catch (SQLException e) {
            throw AppException.internal("Error al buscar Laboratorio por ID: " + e.getMessage());
        }
    }

    public boolean existeLaboratorioPorID(int id_lab) {
        final String sql = "SELECT 1 FROM laboratorio WHERE id_lab = ? LIMIT 1";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id_lab);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw AppException.internal("Error verificando existencia por ID: " + e.getMessage());
        }
    }

    public List<Laboratorio> buscarLaboratorios(String nombre_lab, String ubicacion, int capacidad_personas, int capacidad_equipo,
                                        String estado_lab, LocalDate fecha_registro_lab) {
        List<Laboratorio> laboratorios = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id_lab, nombre_lab, ubicacion, capacidad_personas, capacidad_equipo, estado_lab, fecha_registro_lab FROM laboratorio WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (nombre_lab != null && !nombre_lab.isBlank()) {
            sql.append(" AND nombre_lab ILIKE ?");
            params.add("%" + nombre_lab.trim() + "%");
        }
        if (ubicacion != null && !ubicacion.isBlank()) {
            sql.append(" AND ubicacion ILIKE ?");
            params.add("%" + ubicacion.trim() + "%");
        }
        if (capacidad_personas > 0) {
            sql.append(" AND capacidad_personas = ?");
            params.add(capacidad_personas);
        }
        if (capacidad_equipo > 0) {
            sql.append(" AND capacidad_equipo = ?");
            params.add(capacidad_equipo);
        }
        if (estado_lab != null && !estado_lab.isBlank()) {
            sql.append(" AND estado_lab ILIKE ?");
            params.add("%" + estado_lab.trim() + "%");
        }
        if (fecha_registro_lab != null) {
            sql.append(" AND fecha_registro_lab = ?");
            params.add(Date.valueOf(fecha_registro_lab));
        }

        sql.append(" ORDER BY nombre_lab ASC ");

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    laboratorios.add(mapRowLaboratorio(rs));
                }
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al buscar laboratorios: " + e.getMessage());
        }

        return laboratorios;
    }

    public boolean cambiarEstadoLaboratorio(Laboratorio lab, String nuevoEstado)  {  //Función para cambiar estado laboratorio.
        String sql = "UPDATE laboratorio SET estado_lab = ? WHERE id_lab = ?";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, nuevoEstado);
            ps.setInt(2, lab.getId_lab());
            int rows = ps.executeUpdate();
            if (rows == 0) { throw AppException.notFound("No se pudo cambiar el estado."); }
            if (rows > 0) { // Es para actualizar el objeto en memoria y que sea consistente a la BD.
                lab.setEstado_lab(nuevoEstado);
                return true;
            }
        } catch (SQLException e) {
            throw AppException.internal("Error al cambiar estado del laboratorio" + e.getMessage());
        }
        return false;
    }

    private Laboratorio mapRowLaboratorio(ResultSet rs) {
        try {
            int id_lab = rs.getInt("id_lab");
            String nombre_lab = rs.getString("nombre_lab");
            String ubicacion = rs.getString("ubicacion");
            int capacidad_personas = rs.getInt("capacidad_personas");
            int capacidad_equipo = rs.getInt("capacidad_equipo");
            String estado_lab = rs.getString("estado_lab");
            LocalDate fecha_registro_lab = rs.getObject("fecha_registro_lab", LocalDate.class);

            return new Laboratorio(id_lab, nombre_lab, ubicacion, capacidad_personas, capacidad_equipo,
                    estado_lab, fecha_registro_lab);

        } catch (SQLException e) {
            throw AppException.internal("Error al mapear fila de laboratorio: " + e.getMessage());
        }
    }


}

