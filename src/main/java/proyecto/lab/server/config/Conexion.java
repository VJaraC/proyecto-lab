package proyecto.lab.server.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
/    private static final String URL = "jdbc:postgresql://localhost:5432/prueba";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Zh0ngl11tt0";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD); //en caso de dar error lanza un SQLException
    }

    //este metodo hace que para guardar los cambios en la BD, debemos ingresar conn.commit(), sirve cuando se hacen varias operaciones juntas, insert, update, etc.
    public Connection getTxConnection() throws SQLException {
        Connection c = getConnection();
        c.setAutoCommit(false);
        return c;
    }
}
