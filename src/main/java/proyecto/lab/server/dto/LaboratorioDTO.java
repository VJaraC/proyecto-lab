package proyecto.lab.server.dto;
import proyecto.lab.server.models.Laboratorio;
import java.time.LocalDate;

public record LaboratorioDTO(Integer id_lab, String nombre_lab, String ubicacion, int capacidad_personas, int capacidad_equipo, String estado_lab, LocalDate fecha_registro_lab){

    public LaboratorioDTO( String nombre_lab, String ubicacion, int capacidad_personas, int capacidad_equipo, LocalDate fecha_registro_lab) {
            this(null, nombre_lab,ubicacion,capacidad_personas,capacidad_equipo,null,fecha_registro_lab);
    }

}
