package proyecto.lab.server.controller;

import proyecto.lab.server.dto.ResumenEquipoDTO;
import proyecto.lab.server.service.MetricasService;

import java.util.List;

public class MetricasController {
    private final MetricasService metricasService;

    public MetricasController(MetricasService metricasService) {
        this.metricasService = metricasService;
    }

    public List<ResumenEquipoDTO> obtenerResumenEquipo(){
        return metricasService.obtenerResumenEquipo();
    }
}
