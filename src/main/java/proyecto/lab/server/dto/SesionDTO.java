package proyecto.lab.server.dto;

import proyecto.lab.server.models.Sesion;

import java.time.LocalDate;

public record SesionDTO (
        int id_sesion,
        int id_equipo,
        String horaInicio,
        String horaFin,
        LocalDate fecha,
        String estado
)
{
    public SesionDTO(Sesion sesion){
        this(
                sesion.getId_sesion(),
                sesion.getId_equipo(),
                sesion.getHoraInicio(),
                sesion.getHoraFin(),
                sesion.getFecha(),
                sesion.getEstado()
        );
    }

}
