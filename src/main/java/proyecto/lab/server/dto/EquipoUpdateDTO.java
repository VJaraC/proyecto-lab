package proyecto.lab.server.dto;

import java.time.LocalDate;

public record EquipoUpdateDTO(
        Integer id,
        //datos editables de equipo
        Integer id_lab_equipo,
        String hostname,
        String estado,
        String ip,
        String modeloCPU,
        String nucleosCPU,
        String ramTotal,
        String almacenamiento,
        String modeloGPU

) {
}
