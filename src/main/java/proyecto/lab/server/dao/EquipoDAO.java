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

    public boolean insertarEquipo(Equipo equipo) {
        String sql = "INSERT INTO equipo (rut, id_lab, hostname, numero_serie, fabricante_pc, estado_equipo, modelo, mac, ip, cpu_modelo, cpu_nucleos, ram_total, almacenamiento, gpu_modelo, fecha_ingreso_eq) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, equipo.getId_admin());
            ps.setInt(2, equipo.getId_lab_equipo());
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
            ps.setDate(15, Date.valueOf(equipo.getFecha_ingreso()));

            int filas = ps.executeUpdate();

            if (filas > 0) {
                //Recuperar el ID autogenerado
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        equipo.setId_equipo(idGenerado);  //actualizar el objeto que se recibio
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar equipo: " + e.getMessage());
        }

        return false;
    }


    public Boolean actualizarEquipo(Equipo equipo){
        String sql = "UPDATE equipo SET hostname = ?, estado_equipo = ?, ip = ?, cpu_modelo = ?, cpu_nucleos = ?, ram_total = ?, almacenamiento = ?, gpu_modelo = ? WHERE id_equipo = ?";

        try (Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, equipo.getHostname());
            ps.setString(2, equipo.getEstado());
            ps.setString(3, equipo.getIp());
            ps.setString(4, equipo.getModeloCPU());
            ps.setString(5, equipo.getNucleosCPU());
            ps.setString(6, equipo.getRamTotal());
            ps.setString(7, equipo.getAlmacenamiento());
            ps.setString(8, equipo.getModeloGPU());
            ps.setInt(9, equipo.getId_equipo());

            int filas = ps.executeUpdate();
            if(filas > 0){
                System.out.println("Equipo actualizado exitosamente");
            }
            else{
                System.out.println("No se encontró un equipo con ese ID");
            }
        } catch(SQLException e){
            System.out.println("Error al actualizar equipo: " + e.getMessage());
        }
        return true;
    }



    //tratar de hacer una funcion que se le pase un rs y esta la transforme a un objeto. Hacer el mapear equipo en otra función que tome la función anterior y las agregue a una lista.
    public Equipo buscarEquipoPorId(int id){
        String sql = "SELECT * FROM equipo WHERE id_eq = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id_equipo = rs.getInt("id_eq");
                int id_lab = rs.getInt("id_lab");
                int id_admin = rs.getInt("id");
                String hostname = rs.getString("hostname");
                String numero_serie = rs.getString("numero_serie");
                String fabricante_pc = rs.getString("fabricante_pc");
                String estado_equipo = rs.getString("estado_equipo");
                String modelo = rs.getString("modelo");
                String mac = rs.getString("mac");
                String ip  = rs.getString("ip");
                String cpu_modelo = rs.getString("cpu_modelo");
                String cpu_nucleos = rs.getString("cpu_nucleos");
                String ram_total = rs.getString("ram_total");
                String almacenamiento = rs.getString("almacenamiento");
                String gpu_modelo = rs.getString("gpu_modelo");

                java.sql.Date sqlDate = rs.getDate("fecha_ingreso_eq");
                java.time.LocalDate fecha_ingreso_eq = (sqlDate != null ? sqlDate.toLocalDate() : null);

                return new Equipo(id_equipo, id_lab, id_admin, hostname, numero_serie, fabricante_pc, estado_equipo, modelo, mac, ip, cpu_modelo, cpu_nucleos, ram_total, almacenamiento, gpu_modelo, fecha_ingreso_eq);
            }
        }
        catch (SQLException e){
            System.out.println("Error al buscar equipo: " + e.getMessage());
        }
        return null;
    }


    public List<Equipo> buscarEquipoPorIdLab(int id_lab){
        String sql = "SELECT * FROM equipo WHERE id_equipo = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id_lab);

            try(ResultSet rs = ps.executeQuery()) {
                return mapearEquipos(rs);
            }

        } catch(SQLException e){
            throw new RuntimeException("Error al buscar por ID del Laboratorio",e);
        }
    }



    public Equipo buscarEquipoPorNumSerie(String numSerie){
        String sql = "SELECT * FROM equipo WHERE numero_serie = ?";
        Equipo equipo = null;

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, numSerie);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    equipo = new Equipo();
                    equipo.setId_equipo(rs.getInt("id_eq"));
                    equipo.setId_admin(rs.getInt("id"));
                    equipo.setId_lab_equipo(rs.getInt("id_lab"));
                    equipo.setHostname(rs.getString("hostname"));
                    equipo.setNumero_serie(rs.getString("numero_serie"));
                    equipo.setFabricante(rs.getString("fabricante_pc"));
                    equipo.setEstado(rs.getString("estado_equipo"));
                    equipo.setModelo(rs.getString("modelo"));
                    equipo.setMac(rs.getString("mac"));
                    equipo.setIp(rs.getString("ip"));
                    equipo.setModeloCPU(rs.getString("cpu_modelo"));
                    equipo.setNucleosCPU(rs.getString("cpu_nucleos"));
                    equipo.setRamTotal(rs.getString("ram_total"));
                    equipo.setAlmacenamiento(rs.getString("almacenamiento"));
                    equipo.setModeloGPU(rs.getString("gpu_modelo"));
                    equipo.setFecha_ingreso(rs.getDate("fecha_ingreso_eq").toLocalDate());

                }
            }

        } catch(SQLException e){
            System.out.println("Error al buscar equipo: " + e.getMessage());
        }
        return equipo;
    }


    public List<Equipo> mapearEquipos(ResultSet rs){
        List<Equipo> equipos = new ArrayList<>();
        try{
            while(rs.next()){
                int id = rs.getInt("id_eq");
                int id_lab = rs.getInt("id_lab");
                int id_admin = rs.getInt("id");
                String hostname = rs.getString("hostname");
                String numero_serie = rs.getString("numero_serie");
                String fabricante_pc = rs.getString("fabricante_pc");
                String estado_equipo = rs.getString("estado_equipo");
                String modelo = rs.getString("modelo");
                String mac = rs.getString("mac");
                String ip  = rs.getString("ip");
                String cpu_modelo = rs.getString("cpu_modelo");
                String cpu_nucleos = rs.getString("cpu_nucleos");
                String ram_total = rs.getString("ram_total");
                String almacenamiento = rs.getString("almacenamiento");
                String gpu_modelo = rs.getString("gpu_modelo");

                java.sql.Date sqlDate = rs.getDate("fecha_ingreso_eq");
                java.time.LocalDate fecha_ingreso_eq = (sqlDate != null ? sqlDate.toLocalDate() : null);

                equipos.add(new Equipo(id, id_lab, id_admin, hostname, numero_serie, fabricante_pc, estado_equipo, modelo, mac, ip, cpu_modelo, cpu_nucleos, ram_total, almacenamiento, gpu_modelo, fecha_ingreso_eq));
            }
        }

        catch(SQLException e){
            throw AppException.internal("Error al mapear equipos: "+ e.getMessage());
        }
        return equipos;

    }


}
