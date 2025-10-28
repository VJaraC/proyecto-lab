package proyecto.lab.server.dto;

import java.time.LocalDate;

public record EquipoBusquedaDTO(
        //filtros de busqueda
        Integer id_equipo,
        Integer rut_admin,
        Integer id_lab_equipo,
        String hostname,
        String numero_serie,
        String fabricante,
        String estado,
        String modelo,
        String mac,
        String ip,
        LocalDate fecha_ingreso
)
{

}
