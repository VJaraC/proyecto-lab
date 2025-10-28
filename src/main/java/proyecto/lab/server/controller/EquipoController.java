package proyecto.lab.server.controller;
import proyecto.lab.server.dto.EquipoBusquedaDTO;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.models.Equipo;
import proyecto.lab.server.service.EquipoService;

import java.util.ArrayList;
import java.util.List;

import static proyecto.lab.server.utils.ValidadorUtils.*;


public class EquipoController {
    private final EquipoService equipoService;
    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    public EquipoDTO crearEquipo(EquipoDTO equipo){
        validarNoNulo(equipo, "Datos requeridos");
        return equipoService.crearEquipo(equipo);
    }

    public EquipoDTO buscarEquipo(EquipoBusquedaDTO filtros){
        validarNoNulo(filtros, "Datos requeridos");
        return equipoService.buscarEquipo(filtros);
    }

}
