package proyecto.lab.server.dao;

import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.dto.PuntoGraficoDTO;
import proyecto.lab.server.dto.ResumenEquipoDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.*;

public class MetricasDAO {
    private final Conexion conexion;

    public MetricasDAO() {
        this.conexion = new Conexion();
    }

    public List<ResumenEquipoDTO> obtenerResumenEquipo() {
        List<ResumenEquipoDTO> lista = new ArrayList<>();

        String sql = """
        SELECT 
            E.HOSTNAME,
            COALESCE(cpu.VALOR, 0) as cpu_val,
            COALESCE(ram.VALOR, 0) as ram_val,
            COALESCE(disk.VALOR, 0) as disk_val
        FROM EQUIPO E
        JOIN SESION S ON E.ID_EQ = S.ID_EQ
        
        -- 1. Última CPU
        LEFT JOIN LATERAL (
            SELECT VALOR FROM METRICAS_RAW m 
            JOIN TIPO_METRICAS tm ON m.ID_TIPO = tm.ID_TIPO 
            WHERE m.ID_EQ = E.ID_EQ AND tm.CLAVE = 'cpu_usage' 
            ORDER BY m.FECHA_REGISTRO DESC LIMIT 1
        ) cpu ON true
        
        -- 2. Última RAM
        LEFT JOIN LATERAL (
            SELECT VALOR FROM METRICAS_RAW m 
            JOIN TIPO_METRICAS tm ON m.ID_TIPO = tm.ID_TIPO 
            WHERE m.ID_EQ = E.ID_EQ AND tm.CLAVE = 'ram_usage' 
            ORDER BY m.FECHA_REGISTRO DESC LIMIT 1
        ) ram ON true

        -- ultimo disco
        LEFT JOIN LATERAL (
            SELECT VALOR FROM METRICAS_RAW m 
            JOIN TIPO_METRICAS tm ON m.ID_TIPO = tm.ID_TIPO 
            WHERE m.ID_EQ = E.ID_EQ AND tm.CLAVE = 'disk_usage' 
            ORDER BY m.FECHA_REGISTRO DESC LIMIT 1
        ) disk ON true
        
        WHERE S.ESTADO_SESION = 'ACTIVA' 
        ORDER BY E.HOSTNAME ASC
    """;

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ResumenEquipoDTO(
                        rs.getString("HOSTNAME"),
                        rs.getDouble("cpu_val"),
                        rs.getDouble("ram_val"),
                        rs.getDouble("disk_val") // Leemos la nueva columna
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    public List<PuntoGraficoDTO> obtenerHistorialGrafico(String hostname, String claveMetrica, int limite) {
        List<PuntoGraficoDTO> historial = new ArrayList<>();

        String sql = """
        SELECT 
            to_char(m.FECHA_REGISTRO, 'HH24:MI:SS') as hora_fmt, -- Eje X
            m.VALOR                                              -- Eje Y
        FROM METRICAS_RAW m
        JOIN EQUIPO e ON m.ID_EQ = e.ID_EQ
        JOIN TIPO_METRICAS tm ON m.ID_TIPO = tm.ID_TIPO
        WHERE e.HOSTNAME = ? 
          AND tm.CLAVE = ?
        ORDER BY m.FECHA_REGISTRO DESC -- Traer los más recientes primero
        LIMIT ?
    """;

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hostname);
            ps.setString(2, claveMetrica);
            ps.setInt(3, limite);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historial.add(new PuntoGraficoDTO(
                            rs.getString("hora_fmt"),
                            rs.getDouble("VALOR")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 💡 TRUCO VITAL:
        // La BD nos dio [10:05, 10:04, 10:03...] (Descendente)
        // El gráfico necesita [10:03, 10:04, 10:05...] (Ascendente/Cronológico)
        Collections.reverse(historial);

        return historial;
    }
}
