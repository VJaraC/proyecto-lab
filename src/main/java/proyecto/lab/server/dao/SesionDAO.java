package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Sesion;
import proyecto.lab.server.models.Usuario;
import proyecto.lab.server.models.Equipo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class SesionDAO {
    private final Conexion conexion;

    public SesionDAO(){
        this.conexion = new Conexion();
    }

    public boolean crearSesion(Sesion sesion){
        String sql = "INSERT INTO sesion (id_sesion, id_eq, hora_inicio, hora_termino, fecha, estado_sesion)\n" +
                "values (?, ?, ?, ?, ?, ?);";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, sesion.getId_sesion());
            ps.setInt(2, sesion.getId_equipo());
            ps.setString(3, sesion.getHoraInicio());
            ps.setString(4, sesion.getHoraFin());
            ps.setDate(5, Date.valueOf(sesion.getFecha()));
            ps.setString(6, sesion.getEstado());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                //Recuperar el ID autogenerado
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        sesion.setId_equipo(idGenerado);  //actualizar el objeto que se recibio
                    }
                }
                return true;
            }

        }
        catch(SQLException e){
            System.out.println("Error al insertar equipo: " + e.getMessage());
        }
        return false;
    }
}
