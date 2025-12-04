package proyecto.lab.server.controller;

import proyecto.lab.server.dto.PuntoGraficoDTO;
import proyecto.lab.server.dto.ResumenEquipoDTO;
import proyecto.lab.server.service.MetricasService;

import java.util.List;

public class MetricasController {
    private final MetricasService metricasService;

    public MetricasController(MetricasService metricasService) {
        this.metricasService = metricasService;
    }

    public List<ResumenEquipoDTO> obtenerResumenEquipo() {
        return metricasService.obtenerResumenEquipo();
    }

    public List<PuntoGraficoDTO> obtenerCpu(String hostname) {
        return metricasService.obtenerCpu(hostname);
    }

    public List<PuntoGraficoDTO> obtenerRam(String hostname) {
        return metricasService.obtenerRam(hostname);
    }

    public List<PuntoGraficoDTO> obtenerDisco(String hostname) {
        return metricasService.obtenerDisco(hostname);
    }
}

