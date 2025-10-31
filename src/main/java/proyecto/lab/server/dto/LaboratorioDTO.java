package proyecto.lab.server.dto;
import proyecto.lab.server.models.Laboratorio;
import java.time.LocalDate;

public record LaboratorioDTO(int id_lab, String nombre_lab, String ubicacion, int capacidad_personas, int capacidad_equipo, String estado_lab, LocalDate fecha_registro_lab){

    //constructor
    public LaboratorioDTO(Laboratorio laboratorio) {
        this(
                laboratorio.getId_lab(),
                laboratorio.getNombre_lab(),
                laboratorio.getUbicacion(),
                laboratorio.getCapacidad_personas(),
                laboratorio.getCapacidad_equipo(),
                laboratorio.getEstado_lab(),
                laboratorio.getFecha_registro_lab()
        );
    }
}
