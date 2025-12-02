package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.dto.SesionHoraDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Laboratorio;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SesionDAO {
    private final Conexion conexion;

    public SesionDAO(){
        this.conexion = new Conexion(); }

    public int contarSesionesActivas(){
        String sql = "SELECT count(*) as cantidad_sesiones_activas\n" +
                "    FROM sesion WHERE estado_sesion='ACTIVA';";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar por sesiones activas", e);
        }
        return 0;
    }

    public int contarSesionesTotales(){
        String sql = "SELECT count(*) as cantidad_sesiones_totales FROM sesion";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar las sesiones", e);
        }
        return 0;
    }

    public List<SesionHoraDTO> obtenerSesionesPorHora() {
        List<SesionHoraDTO> reporte = new ArrayList<>();

        String sql = """
        WITH horas_del_dia AS (
            SELECT generate_series(
                CURRENT_DATE::timestamp, 
                CURRENT_DATE::timestamp + '23 hours'::interval, 
                '1 hour'::interval
            ) AS hora_ref
        )
        SELECT 
            to_char(h.hora_ref, 'HH24:00') AS hora,
            COUNT(s.ID_SESION) AS sesiones_activas
        FROM horas_del_dia h
        LEFT JOIN SESION s ON 
            s.FECHA_INICIO < (h.hora_ref + '1 hour'::interval)
            AND 
            (s.FECHA_TERMINO >= h.hora_ref OR s.ESTADO_SESION = 'ACTIVA')
        GROUP BY h.hora_ref
        ORDER BY h.hora_ref
    """;

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                //crear dto sesion hora
                String hora = rs.getString("hora");
                int cantidad = rs.getInt("sesiones_activas");

                //agregarlo a la lista
                reporte.add(new SesionHoraDTO(hora, cantidad));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reporte;
    }



}

