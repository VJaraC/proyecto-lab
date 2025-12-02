package proyecto.lab.server.controller;

import proyecto.lab.server.dto.SesionHoraDTO;
import proyecto.lab.server.service.EquipoService;
import proyecto.lab.server.service.SesionService;

import java.util.List;

public class SesionController {
    private final SesionService sesionService;
    
    public SesionController(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    public int contarSesionesActivas(){
        return sesionService.contarSesionesActivas();
    }

    public int contarSesionesTotales(){
        return sesionService.contarSesionesTotales();
    }
    
    public List<SesionHoraDTO> obtenerSesionesPorHora(){
        return sesionService.obtenerSesionesPorHora();
    }
}
