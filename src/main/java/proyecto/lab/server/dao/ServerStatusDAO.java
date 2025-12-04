package proyecto.lab.server.dao;

import proyecto.lab.server.config.Conexion; // Reutiliza tu conexión si es compartida

import java.sql.*;

public class ServerStatusDAO {

    private final Conexion conexion;

    public ServerStatusDAO() {
        this.conexion = new Conexion();
    }

    public boolean isServerOnline() {
        // Pedimos las fechas explícitas para verlas con nuestros propios ojos
        String sql = """
        SELECT 
            LAST_SEEN, 
            CURRENT_TIMESTAMP as AHORA_BD,
            (LAST_SEEN > (CURRENT_TIMESTAMP - INTERVAL '10 seconds')) as is_alive 
        FROM SERVER_STATUS 
        WHERE ID = 1
    """;

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Timestamp ultimoVisto = rs.getTimestamp("LAST_SEEN");
                Timestamp ahoraBd = rs.getTimestamp("AHORA_BD");
                boolean vivo = rs.getBoolean("is_alive");

                // 🛑 IMPRIME ESTO EN CONSOLA
                System.out.println("--- DIAGNÓSTICO HEARTBEAT ---");
                System.out.println("BD Dice - Último Latido: " + ultimoVisto);
                System.out.println("BD Dice - Hora Actual:   " + ahoraBd);
                System.out.println("¿Diferencia < 10s?:      " + vivo);
                System.out.println("-----------------------------");

                return vivo;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}