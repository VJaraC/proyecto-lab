package proyecto.lab.server.controller;

import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.service.EquipoService;

public class EquipoController {
    private final EquipoService equipoService;
    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    public EquipoDTO crearEquipo(EquipoDTO equipo){

        return equipoService.crearEquipo(equipo);
    }
}
