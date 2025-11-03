package proyecto.lab.server.dto;

import proyecto.lab.server.models.Rol;

import java.io.Serializable;

public record LaboratorioUpdateDTO(
        int id_lab,
        String nombre_lab,
        String ubicacion,
        Integer capacidad_personas,
        Integer capacidad_equipo,
        String estado_lab

)
{
}