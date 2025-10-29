package proyecto.lab.server.controller;
import proyecto.lab.server.dto.EquipoBusquedaDTO;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.EquipoUpdateDTO;
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

    //llamar cuando los filtros retornen solo 1 equipo
    public EquipoDTO buscarEquipo(EquipoBusquedaDTO filtros){
        validarNoNulo(filtros, "Datos requeridos");
        if(filtros.id_equipo() != null || filtros.numero_serie() != null){
            return equipoService.buscarEquipo(filtros);
        }
        else{
            return equipoService.buscarEquipo(filtros);
        }
    }

    public EquipoDTO actualizarEquipo(EquipoUpdateDTO actualizarDTO){
        return equipoService.actualizarEquipo(actualizarDTO);
    }

}
