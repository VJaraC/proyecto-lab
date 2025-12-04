package proyecto.lab.server.dao;

import proyecto.lab.server.config.Conexion; // Ajusta tu paquete de conexión
import proyecto.lab.server.dto.AlertaDetalleDTO;
import java.sql.*;

public class AlertaDAO {

    private final Conexion conexion;

    public AlertaDAO() {
        this.conexion = new Conexion();
    }

    // Función 1: Conteo de alertas en la última hora
    public int contarAlertasUltimaHora() {
        String sql = "SELECT COUNT(*) FROM ALERTA WHERE FECHA_ALERTA >= NOW() - INTERVAL '1 hour'";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al contar alertas recientes", e);
        }
    }

    // Función 2: Obtener la última alerta con detalles (JOINs)
    public AlertaDetalleDTO obtenerUltimaAlertaDetallada() {
        String sql = """
        SELECT
            A.ID_ALERTA,
            E.HOSTNAME,
            TM.NOMBRE_METRICA,
            A.VALOR_REGISTRADO,
            A.MENSAJE,
            A.FECHA_ALERTA
        FROM ALERTA A
        JOIN EQUIPO E ON A.ID_EQ = E.ID_EQ
        JOIN TIPO_METRICAS TM ON A.ID_TIPO = TM.ID_TIPO
        ORDER BY A.FECHA_ALERTA DESC
        LIMIT 1
    """;

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                // Caso A: Hay datos reales
                return new AlertaDetalleDTO(
                        rs.getInt("ID_ALERTA"),
                        rs.getString("HOSTNAME"),
                        rs.getString("NOMBRE_METRICA"),
                        rs.getDouble("VALOR_REGISTRADO"),
                        rs.getString("MENSAJE"),
                        rs.getTimestamp("FECHA_ALERTA")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la última alerta", e);
        }

        // 💡 CASO B: NO HAY DATOS (Retornar objeto "Vacío" por defecto)
        return new AlertaDetalleDTO(
                0,                      // ID 0
                "Sin Datos",            // Hostname
                "-",                    // Métrica
                0.0,                    // Valor
                "No hay alertas registradas", // Mensaje amigable
                new java.sql.Timestamp(System.currentTimeMillis()) // Fecha actual para que no falle el gráfico/tabla
        );
    }
}