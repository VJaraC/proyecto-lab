package proyecto.lab.server.service;

import proyecto.lab.server.dao.EquipoDAO;
import proyecto.lab.server.dto.EquipoBusquedaDTO;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Equipo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static proyecto.lab.server.utils.ValidadorUtils.validarNoNulo;

public class EquipoService {
    private final EquipoDAO equipoDAO;

    public EquipoService(EquipoDAO equipoDAO) { this.equipoDAO = equipoDAO; }

    public EquipoDTO crearEquipo(EquipoDTO equipo) {

        //verificar que el dto contenga información
        if (equipo == null) {
            throw AppException.badRequest("DTO vacío");
        }

        //verificar que el valor de estado sea valido
        String estado = equipo.estado().toLowerCase();
        Set<String> estadosPermitidos = Set.of("operativo", "disponible", "fuera de servicio"); //estados del equipo
        if (!estadosPermitidos.contains(estado)) {
            throw AppException.badRequest("Ingrese un valor válido para estado");
        }

        //buscar un equipo existente
        String numSerie = equipo.numero_serie();
        Equipo equipoExistente = equipoDAO.buscarEquipoPorNumSerie(numSerie);
        if (equipoExistente != null) {
            throw AppException.badRequest("Equipo ya existente");
        }

        //crear el nuevo equipo
        Equipo equipoNuevo =  new Equipo(equipo); //se le pasa el dto que se le entrego como parametro a la función
        boolean insercion = equipoDAO.insertarEquipo(equipoNuevo);
        if(!insercion){
            System.out.println("Error al crear equipo");
        }
        return new EquipoDTO(equipoNuevo); //se le pasa al dto el equipo (modelo) que se creó
    }

    public List<EquipoDTO> buscarEquipo(EquipoBusquedaDTO filtros){
        validarNoNulo(filtros, "Datos requeridos");
        List<Equipo> resultados = new ArrayList<>();

        if(filtros.id_equipo() != null){
            resultados = equipoDAO.buscarPorId(filtros.id_equipo());
        } else if(filtros.id_lab_equipo() != null){
            resultados = equipoDAO.buscarEquipoPorIdLab(filtros.id_lab_equipo());
        }

        return resultados.stream()
                .map(EquipoDTO::new)
                .toList();
    }
}
