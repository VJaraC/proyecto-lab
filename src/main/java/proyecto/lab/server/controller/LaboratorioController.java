package proyecto.lab.server.controller;


import proyecto.lab.server.dto.LaboratorioBusquedaDTO;
import proyecto.lab.server.dto.LaboratorioDTO;
import proyecto.lab.server.service.LaboratorioService;

import static proyecto.lab.server.utils.ValidadorUtils.validarNoNulo;

public class LaboratorioController {
    private final LaboratorioService laboratorioService;
    public LaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    public LaboratorioDTO crearLaboratorio(LaboratorioDTO laboratorio){
        validarNoNulo(laboratorio, "Datos requeridos");
        return laboratorioService.crearLaboratorio(laboratorio);
    }

}
