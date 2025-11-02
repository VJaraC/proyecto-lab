package proyecto.lab.server.dto;

import proyecto.lab.server.models.Equipo;

import java.time.LocalDate;

public record EquipoDTO(
        Integer id_equipo,
        int id_admin,
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
        LocalDate fecha_ingreso,
        String nombreLab) {

    public EquipoDTO(
            int id_equipo,
            int id_admin,
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
            LocalDate fecha_ingreso
    ) {
        this(id_equipo, id_admin, id_lab_equipo, hostname, numero_serie, fabricante, estado,
                modelo, mac, ip, modeloCPU, nucleosCPU, ramTotal, almacenamiento, modeloGPU,
                fecha_ingreso, null);
    }

    public EquipoDTO(
            int id_admin,
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
            LocalDate fecha_ingreso
    ) {
        this(null, id_admin, id_lab_equipo, hostname, numero_serie, fabricante, estado,
                modelo, mac, ip, modeloCPU, nucleosCPU, ramTotal, almacenamiento, modeloGPU,
                fecha_ingreso, null);
    }


}
