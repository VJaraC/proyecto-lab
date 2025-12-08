package proyecto.lab.server.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;
import io.github.cdimascio.dotenv.Dotenv;

public class Conexion {

    // 1. 🕒 BLOQUE ESTÁTICO: Esto se ejecuta una sola vez al iniciar la app.
    // Obliga a que todo el programa Java (Logs, Fechas) use hora de Chile.
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("🕒 Zona Horaria de Java forzada a: " + TimeZone.getDefault().getID());
    }

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .directory("./")
            .filename(".env")
            .load();

    public Connection getConnection() throws SQLException {
        String envUrl = dotenv.get("DB_URL");
        String envUser = dotenv.get("DB_USER");
        String envPass = dotenv.get("DB_PASSWORD");

        String finalUrl;
        String finalUser;
        String finalPass;

        // Lógica de Respaldo
        if (envUrl != null && !envUrl.isEmpty()) {
            finalUrl = envUrl;
            finalUser = envUser;
            finalPass = envPass;
        } else {
            // Datos Hardcoded (Respaldo)
            finalUrl = "jdbc:postgresql://localhost:15432/proyecto_lab";
            finalUser = "proyecto_lab";
            finalPass = "proyectolab124";
        }

        // 2. 🌍 ARREGLO DE URL BD: Forzar a PostgreSQL a usar Chile
        if (!finalUrl.contains("timezone")) {
            if (finalUrl.contains("?")) {
                finalUrl += "&options=-c%20timezone=America/Santiago";
            } else {
                finalUrl += "?options=-c%20timezone=America/Santiago";
            }
        }

        // 🕵️‍♂️ CHIVATO: Imprime esto para ver qué está pasando realmente
        // System.out.println("🔌 Conectando a BD: " + finalUrl);

        try {
            return DriverManager.getConnection(finalUrl, finalUser, finalPass);
        } catch (SQLException e) {
            System.err.println("❌ Error crítico conectando a BD: " + e.getMessage());
            throw e;
        }
    }

    public Connection getTxConnection() throws SQLException {
        Connection c = getConnection();
        c.setAutoCommit(false);
        return c;
    }
}