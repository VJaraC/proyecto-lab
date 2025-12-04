package proyecto.lab.server.dto;

// Solución en ResumenEquipoDTO.java

public record ResumenEquipoDTO(
        String hostname,
        double cpuActual,
        double ramActual,
        double diskActual

) {
    // ⬇️ INCLUIR ESTOS MÉTODOS PARA JAVA FX ⬇️

    // Getter para 'hostname'
    public String getHostname() {
        return hostname;
    }

    // Getter para 'cpuActual'
    public double getCpuActual() {
        return cpuActual;
    }

    // Getter para 'ramActual'
    public double getRamActual() {
        return ramActual;
    }

    // Getter para 'diskActual'
    public double getDiskActual() {
        return diskActual;
    }
    // ⬆️ FIN DE MÉTODOS A INCLUIR ⬆️
}