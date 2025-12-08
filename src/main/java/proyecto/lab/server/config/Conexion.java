package proyecto.lab.server.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;
import io.github.cdimascio.dotenv.Dotenv;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Conexion {

    // El DataSource es el "Pool" que guardará las conexiones abiertas.
    // Es static para que sea compartido por toda la aplicación.
    private static final HikariDataSource dataSource;

    static {
        // 1. 🕒 Configuración Global de Java
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("🕒 Zona Horaria de Java forzada a: " + TimeZone.getDefault().getID());

        // 2. Cargar Dotenv
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .directory("./")
                .filename(".env")
                .load();

        // 3. 🧠 Lógica de Credenciales (Respaldo)
        String envUrl = dotenv.get("DB_URL");
        String finalUrl, finalUser, finalPass;

        if (envUrl != null && !envUrl.isEmpty()) {
            finalUrl = envUrl;
            finalUser = dotenv.get("DB_USER");
            finalPass = dotenv.get("DB_PASSWORD");
        } else {
            // Datos Hardcoded (Respaldo local)
            finalUrl = "jdbc:postgresql://localhost:15432/proyecto_lab";
            finalUser = "proyecto_lab";
            finalPass = "proyectolab124";
            System.out.println("⚠️ Usando credenciales de respaldo (Hardcoded)");
        }

        // 4. 🌍 Arreglo de Timezone para PostgreSQL
        if (!finalUrl.contains("timezone")) {
            if (finalUrl.contains("?")) {
                finalUrl += "&options=-c%20timezone=America/Santiago";
            } else {
                finalUrl += "?options=-c%20timezone=America/Santiago";
            }
        }

        // 5. 🚀 Configuración de HikariCP (El Pool)
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(finalUrl);
        config.setUsername(finalUser);
        config.setPassword(finalPass);

        // --- Optimizaciones del Pool ---
        config.setMaximumPoolSize(10);      // Máx 10 conexiones simultáneas
        config.setMinimumIdle(2);           // Siempre tener 2 listas para usar
        config.setIdleTimeout(30000);       // Cerrar si no se usan en 30s
        config.setConnectionTimeout(30000); // Esperar máx 30s si el pool está lleno
        config.setPoolName("HikariPool-ProyectoLab");

        // --- Optimizaciones de PostgreSQL ---
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        // Crear el DataSource final
        dataSource = new HikariDataSource(config);
        System.out.println("✅ Pool de conexiones HikariCP iniciado correctamente.");
    }

    // Este método ahora es ultrarrápido (aprox 0-1ms)
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public Connection getTxConnection() throws SQLException {
        Connection c = getConnection();
        c.setAutoCommit(false);
        return c;
    }

    // Método opcional para cerrar el pool al cerrar la app
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}