package proyecto.lab.server.service;

import proyecto.lab.server.dao.MetricasDAO;
import proyecto.lab.server.dto.ResumenEquipoDTO;

import java.util.List;

public class MetricasService {
    private final MetricasDAO metricasDAO;

    public MetricasService(MetricasDAO metricasDAO){
        this.metricasDAO = metricasDAO;
    }

    public List<ResumenEquipoDTO> obtenerResumenEquipo(){
        return metricasDAO.obtenerResumenEquipo();
    }
}
