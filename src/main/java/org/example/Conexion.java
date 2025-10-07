package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/prueba";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Zh0ngl11tt0";

    public static Connection getConnection() throws SQLException{
        return  DriverManager.getConnection(URL,USER,PASSWORD);
    }

    public static Connection getTxConnection() throws SQLException{
        Connection c = getConnection();
        c.setAutoCommit(false);
        return c;
    }
}
