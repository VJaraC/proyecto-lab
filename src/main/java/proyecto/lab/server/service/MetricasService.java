package proyecto.lab.server.service;

import proyecto.lab.server.dao.MetricasDAO;
import proyecto.lab.server.dto.PuntoGraficoDTO;
import proyecto.lab.server.dto.ResumenEquipoDTO;

import java.util.List;

public class MetricasService {
    private final MetricasDAO metricasDAO;

    public MetricasService(MetricasDAO metricasDAO){
        this.metricasDAO = metricasDAO;
    }
    //funcion para mostrar la foto de los equipos (parte izquierda monitoreo tiempo real)
    public List<ResumenEquipoDTO> obtenerResumenEquipo(){
        return metricasDAO.obtenerResumenEquipo();
    }

    //funciones para el grafico (parte derecha)
    public List<PuntoGraficoDTO> obtenerCpu(String hostname){
        return metricasDAO.obtenerHistorialGrafico(hostname, "cpu_usage", 20);
    }

    public List<PuntoGraficoDTO> obtenerRam(String hostname){
        return metricasDAO.obtenerHistorialGrafico(hostname, "ram_usage", 20);
    }

    public List<PuntoGraficoDTO> obtenerCpuTemp(String hostname){
        return metricasDAO.obtenerHistorialGrafico(hostname, "cpu_temp", 20);
    }

    public List<PuntoGraficoDTO> obtenerDisco(String hostname){
        return metricasDAO.obtenerHistorialGrafico(hostname, "disk_activity", 20);
    }
}
