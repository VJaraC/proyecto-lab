package proyecto.lab.server.controller;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.service.EquipoService;
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
}
