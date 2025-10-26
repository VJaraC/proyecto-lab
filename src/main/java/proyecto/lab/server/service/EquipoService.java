package proyecto.lab.server.service;

import proyecto.lab.server.dao.EquipoDAO;
import proyecto.lab.server.dto.EquipoDTO;

public class EquipoService {
    private final EquipoDAO equipoDAO;

    public EquipoService(EquipoDAO equipoDAO) { this.equipoDAO = equipoDAO; }

    public EquipoDTO crearEquipo(EquipoDTO equipo){
        return equipo;
    }
}
