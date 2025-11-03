package proyecto.lab.server.dto;

public record EquipoCountDTO(
        long disponibles,
        long operativos,
        long fueraServicio,
        long total
) {}
