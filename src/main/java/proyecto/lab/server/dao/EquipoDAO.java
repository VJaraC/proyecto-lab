package proyecto.lab.server.dao;
import proyecto.lab.server.config.Conexion;
import proyecto.lab.server.dto.EquipoCountDTO;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Usuario;
import proyecto.lab.server.models.Equipo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class EquipoDAO {
    private final Conexion conexion;

    public EquipoDAO() {
        this.conexion = new Conexion();
    }

    public boolean insertarEquipo(Equipo equipo) {
        String sql = "INSERT INTO equipo (id, id_lab, hostname, numero_serie, fabricante_pc, estado_equipo, modelo, mac, ip, cpu_modelo, cpu_nucleos, ram_total, almacenamiento, gpu_modelo, fecha_ingreso_eq) " +
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


    public Boolean actualizarEquipo(Equipo equipo) {
        String sql = "UPDATE equipo SET id_lab = ?, hostname = ?, estado_equipo = ?, ip = ?, cpu_modelo = ?, cpu_nucleos = ?, ram_total = ?, almacenamiento = ?, gpu_modelo = ? WHERE id_eq = ?";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, equipo.getId_lab_equipo());
            ps.setString(2, equipo.getHostname());
            ps.setString(3, equipo.getEstado());
            ps.setString(4, equipo.getIp());
            ps.setString(5, equipo.getModeloCPU());
            ps.setString(6, equipo.getNucleosCPU());
            ps.setString(7, equipo.getRamTotal());
            ps.setString(8, equipo.getAlmacenamiento());
            ps.setString(9, equipo.getModeloGPU());
            ps.setInt(10, equipo.getId_equipo());


            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Equipo actualizado exitosamente");
                return true;
            }
            System.out.println("No se encontró un equipo con ese ID");
        } catch (SQLException e) {
            System.out.println("Error al actualizar equipo: " + e.getMessage());
        }
        return false;
    }

    //tratar de hacer una funcion que se le pase un rs y esta la transforme a un objeto. Hacer el mapear equipo en otra función que tome la función anterior y las agregue a una lista.
    public Equipo buscarEquipoPorId(int id) {
        String sql = "SELECT * FROM equipo WHERE id_eq = ?";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id_equipo = rs.getInt("id_eq");
                int id_lab = rs.getInt("id_lab");
                int id_admin = rs.getInt("id");
                String hostname = rs.getString("hostname");
                String numero_serie = rs.getString("numero_serie");
                String fabricante_pc = rs.getString("fabricante_pc");
                String estado_equipo = rs.getString("estado_equipo");
                String modelo = rs.getString("modelo");
                String mac = rs.getString("mac");
                String ip = rs.getString("ip");
                String cpu_modelo = rs.getString("cpu_modelo");
                String cpu_nucleos = rs.getString("cpu_nucleos");
                String ram_total = rs.getString("ram_total");
                String almacenamiento = rs.getString("almacenamiento");
                String gpu_modelo = rs.getString("gpu_modelo");

                java.sql.Date sqlDate = rs.getDate("fecha_ingreso_eq");
                java.time.LocalDate fecha_ingreso_eq = (sqlDate != null ? sqlDate.toLocalDate() : null);

                return new Equipo(id_equipo, id_lab, id_admin, hostname, numero_serie, fabricante_pc, estado_equipo, modelo, mac, ip, cpu_modelo, cpu_nucleos, ram_total, almacenamiento, gpu_modelo, fecha_ingreso_eq);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar equipo: " + e.getMessage());
        }
        return null;
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

    public List<Equipo> buscarEquipoPorIdLab(int id_lab){
        String sql = "SELECT * FROM equipo WHERE id_lab = ?";

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

    public List<Equipo> buscarPorIdAdmin(int idAdmin) {
        String sql =  "SELECT * FROM equipo WHERE id = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, idAdmin);

            try(ResultSet rs = ps.executeQuery()){
                return mapearEquipos(rs);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Error al buscar por Admin",e);
        }
    }

    public List<Equipo> buscarPorFechaIngreso(LocalDate fechaIngreso) {
        String sql =  "SELECT * FROM equipo WHERE fecha_ingreso_eq = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDate(1, Date.valueOf(fechaIngreso));

            try(ResultSet rs = ps.executeQuery()){
                return mapearEquipos(rs);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Error al buscar por fecha de ingreso",e);
        }
    }

    public List<Equipo> buscarEquipoPorHostname(String hostname){
        String sql = "SELECT * FROM equipo WHERE hostname = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, hostname);
            try(ResultSet rs = ps.executeQuery()) {
                return mapearEquipos(rs);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Error al buscar por Hostname",e);
        }
    }

    public List<Equipo> buscarEquipoPorModelo(String hostname) {
        String sql = "SELECT * FROM equipo WHERE modelo = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, hostname);
            try(ResultSet rs = ps.executeQuery()){
                return mapearEquipos(rs);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Error al buscar por Modelo",e);
        }
    }

    public List<Equipo> buscarPorFabricante(String fabricante) {
        String sql =  "SELECT * FROM equipo WHERE fabricante_pc = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, fabricante);

            try(ResultSet rs = ps.executeQuery()){
                return mapearEquipos(rs);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Error al buscar por Fabricante",e);
        }
    }

    public List<Equipo> buscarPorMac(String Mac) {
        String sql =  "SELECT * FROM equipo WHERE mac = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, Mac);

            try(ResultSet rs = ps.executeQuery()){
                return mapearEquipos(rs);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Error al buscar por Mac",e);
        }
    }

    public List<Equipo> buscarPorIp(String Ip) {
        String sql =  "SELECT * FROM equipo WHERE ip = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, Ip);

            try(ResultSet rs = ps.executeQuery()){
                return mapearEquipos(rs);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Error al buscar por IP",e);
        }
    }

    public List<Equipo> buscarPorEstado(String Estado) {
        String sql =  "SELECT * FROM equipo WHERE estado_equipo = ?";

        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, Estado);

            try(ResultSet rs = ps.executeQuery()){
                return mapearEquipos(rs);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Error al buscar por Estado",e);
        }
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

    public List<Equipo> mostrarEquipos() {
        String sql = "SELECT equipo.*, nombre_lab FROM equipo\n" +
                "join laboratorio on equipo.id_lab = laboratorio.id_lab";
        try(Connection conn = conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            return mapearEquipos(rs);
        }
        catch(SQLException e){
            throw new RuntimeException("Error al mostrar equipos",e);
        }
    }

    public List<EquipoDTO> listarEquiposDTO() {
        String sql = "SELECT equipo.*, nombre_lab FROM equipo\n" +
                "join laboratorio on equipo.id_lab = laboratorio.id_lab";

        List<EquipoDTO> lista = new ArrayList<>();
        try (Connection c = conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new EquipoDTO(
                        rs.getInt("id_eq"),
                        rs.getInt("id_lab"),
                        rs.getInt("id"),
                        rs.getString("hostname"),
                        rs.getString("numero_serie"),
                        rs.getString("fabricante_pc"),
                        rs.getString("estado_equipo"),
                        rs.getString("modelo"),
                        rs.getString("mac"),
                        rs.getString("ip"),
                        rs.getString("cpu_modelo"),
                        rs.getString("cpu_nucleos"),
                        rs.getString("ram_total"),
                        rs.getString("almacenamiento"),
                        rs.getString("gpu_modelo"),

                        rs.getDate("fecha_ingreso_eq").toLocalDate(),

                        rs.getString("nombre_lab")
                ));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar equipos DTO", ex);
        }
        return lista;
    }

    //funcion para obtener la cantidad de equipos por estado (exclusivo postgresql por "filter")
    public EquipoCountDTO contarEquiposResumen(Integer idLab) {
        String base = """
        SELECT
          COUNT(*) FILTER (WHERE estado_equipo = 'disponible')       AS disponibles,
          COUNT(*) FILTER (WHERE estado_equipo = 'operativo')        AS operativos,
          COUNT(*) FILTER (WHERE estado_equipo = 'fuera de servicio') AS fuera_servicio,
          COUNT(*)                                                  AS total
        FROM equipo
    """;
        String sql = (idLab == null) ? base : base + " WHERE id_lab = ?";

        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (idLab != null) ps.setInt(1, idLab);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EquipoCountDTO(
                            rs.getLong("disponibles"),
                            rs.getLong("operativos"),
                            rs.getLong("fuera_servicio"),
                            rs.getLong("total")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar equipos por estado", e);
        }
        return new EquipoCountDTO(0,0,0,0);
    }


}
