package proyecto.lab.server.dto;

public record ResumenEquipoDTO(
         String hostname,
         double cpuActual,
         double ramActual,
         double diskActual

) {}
