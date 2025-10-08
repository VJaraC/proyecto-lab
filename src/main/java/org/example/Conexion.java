package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://magallanes.icci-unap.cl:5432/alhidalgog";
    private static final String USER = "alhidalgog";
    private static final String PASSWORD = "alhidalgog_DB2025";

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
