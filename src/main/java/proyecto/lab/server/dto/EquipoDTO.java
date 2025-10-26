package proyecto.lab.server.dto;

import proyecto.lab.server.models.Equipo;

import java.time.LocalDate;

public record EquipoDTO(
        int id_equipo,
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
        String almacenamiento,
        String modeloGPU,
        LocalDate fecha_ingreso) {

    public EquipoDTO(Equipo equipo) {
        this(
                equipo.getId_equipo(),
                equipo.getRut_admin(),
                equipo.getId_lab_equipo(),
                equipo.getHostname(),
                equipo.getNumero_serie(),
                equipo.getFabricante(),
                equipo.getEstado(),
                equipo.getModelo(),
                equipo.getMac(),
                equipo.getIp(),
                equipo.getModeloCPU(),
                equipo.getNucleosCPU(),
                equipo.getRamTotal(),
                equipo.getAlmacenamiento(),
                equipo.getModeloGPU(),
                equipo.getFecha_ingreso()
        );

    }
}
