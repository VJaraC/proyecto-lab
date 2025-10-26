package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Usuario;
import proyecto.lab.server.models.Equipo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class EquipoDAO {
    private final Conexion conexion;

    public EquipoDAO() { this.conexion = new Conexion(); }

    public Equipo insertarEquipo(Equipo equipo, Usuario user){
        String sql = "INSERT INTO equipo (rut, id_lab, hostname, numero_serie, fabricante_pc, estado_equipo, modelo, mac, ip, cpu_modelo, cpu_nucleos, ram_total, almacenamiento, gpu_modelo) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, equipo.getRut_admin()); //recuperar rut del usuario en sesion (manejar como se le pasa el rut del usuario en sesion en el service)
            ps.setInt(2, equipo.getId_lab_equipo()); //luego agregarle el id del lab correspondiente.
            ps.setString(3, equipo.getHostname());
            ps.setString(4, equipo.getNumero_serie());
            ps.setString(5, equipo.getFabricante());
            ps.setString(6, equipo.getEstado());
            ps.setString(7, equipo.getModelo());
            ps.setString(8, equipo.getMac());
            ps.setString(9, equipo.getIp());
            ps.setString(10, equipo.getModeloCPU());
            ps.setString(11, equipo.getNucleosCPU());
            ps.setString(12, equipo.getRamTotal());
            ps.setString(13, equipo.getAlmacenamiento());
            ps.setString(14, equipo.getModeloGPU());
            ps.executeUpdate();


        } catch(SQLException e){
            System.out.println("Error al insertar equipo: " + e.getMessage());
        }
        return equipo;
    }

}
