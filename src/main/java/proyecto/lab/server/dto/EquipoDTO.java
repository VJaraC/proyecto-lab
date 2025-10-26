package proyecto.lab.server.dto;

public record EquipoDTO(int id_equipo,
        String rut_admin,
        int id_lab_equipo,
        String hostname,
        String numero_serie,
        String fabricante,
        String estado,
        String modelo,
        String mac,
        String ip,
        String modeloCPU,
        String nucleosCPU,
        String ramTotal,
        String almacenamiento) {

}
