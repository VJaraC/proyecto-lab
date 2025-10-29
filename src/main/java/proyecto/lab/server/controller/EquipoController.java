package proyecto.lab.server.controller;
import proyecto.lab.server.dto.EquipoBusquedaDTO;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.EquipoUpdateDTO;
import proyecto.lab.server.models.Equipo;
import proyecto.lab.server.service.EquipoService;

import java.time.LocalDate;
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
        return null;

    }

    public EquipoDTO buscarEquipoPorId(int id){
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(id, null, null, null,  null, null, null, null, null, null, null);
        return equipoService.buscarEquipo(filtros);
    }

    public EquipoDTO buscarEquipoPorNumSerie(String numSerie){
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, null, null,  numSerie, null, null, null, null, null, null);
        return equipoService.buscarEquipo(filtros);
    }


    //llamar cuando los filtros puedan retornar mas de un equipo
    public List<EquipoDTO> buscarEquipos(EquipoBusquedaDTO filtros){
        validarNoNulo(filtros, "Datos requeridos");
        if(filtros.id_admin() != null || filtros.id_lab_equipo() != null
                || filtros.hostname() != null || filtros.fabricante() != null
                || filtros.estado() != null || filtros.modelo() != null
                || filtros.mac() != null || filtros.ip() != null
                || filtros.fecha_ingreso() != null ){

            return equipoService.buscarEquipos(filtros);
        }
        return null;
    }

    public List<EquipoDTO> buscarEquipoPorIdAdmin(int idAdmin){
        validarNoNulo(idAdmin, "Id Admin requerido");
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, idAdmin, null, null,  null, null, null, null, null, null, null);
        return equipoService.buscarEquipos(filtros);
    }

    public List<EquipoDTO> buscarEquipoPorIdLab(int idLab){
        validarNoNulo(idLab, "Id Laboratorio requerido");
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, idLab, null,  null, null, null, null, null, null, null);
        return equipoService.buscarEquipos(filtros);
    }

    public List<EquipoDTO> buscarEquipoPorHostname(String hostname){
        validarTexto(hostname, "El hostname es obligatorio");
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, null, hostname,  null, null, null, null, null, null, null);
        return equipoService.buscarEquipos(filtros);
    }

    public List<EquipoDTO> buscarEquipoPorFabricante(String fabricante){
        validarTexto(fabricante, "El fabricante es obligatorio");
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, null, null,  null, fabricante, null, null, null, null, null);
        return equipoService.buscarEquipos(filtros);
    }

    public List<EquipoDTO> buscarEquipoPorIdAdmin(String estado){
        validarTexto(estado, "El estado es obligatorio");
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, null, null,  null, null, estado, null, null, null, null);
        return equipoService.buscarEquipos(filtros);
    }

    public List<EquipoDTO> buscarEquipoPorModelo(String modelo){
        validarTexto(modelo, "El modelo es obligatorio");
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, null, null,  null, null, null, modelo, null, null, null);
        return equipoService.buscarEquipos(filtros);
    }

    public List<EquipoDTO> buscarEquipoPorMac(String mac){
        validarTexto(mac, "El mac es obligatorio");
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, null, null,  null, null, null, null, mac, null, null);
        return equipoService.buscarEquipos(filtros);
    }

    public List<EquipoDTO> buscarEquipoPorIP(String ip){
        validarTexto(ip, "El ip es obligatorio");
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, null, null,  null, null, null, null, null, ip, null);
        return equipoService.buscarEquipos(filtros);
    }

    public List<EquipoDTO> buscarEquipoPorFechaIngreso(LocalDate fechaIngreso){
        validarNoNulo(fechaIngreso, "Fecha ingreso es obligatorio");
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, null, null,  null, null, null, null, null, null, fechaIngreso);
        return equipoService.buscarEquipos(filtros);
    }

    public EquipoDTO actualizarEquipo(EquipoUpdateDTO actualizarDTO){
        validarNoNulo(actualizarDTO, "Campos de edición vacio");
        return equipoService.actualizarEquipo(actualizarDTO);
    }

}
