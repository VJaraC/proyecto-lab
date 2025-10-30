package proyecto.lab.server.controller;
import proyecto.lab.server.dto.*;
import proyecto.lab.server.service.EquipoService;

import java.time.LocalDate;
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


    //funciones para buscar equipo
    public EquipoDTO buscarEquipoPorId(int id){
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(id, null, null, null, null, null, null, null, null, null, null);
        return equipoService.buscarEquipo(filtros);
    }

    public EquipoDTO buscarEquipoPorNumSerie(String numSerie){
        EquipoBusquedaDTO filtros = new EquipoBusquedaDTO(null, null, null, null,  numSerie, null, null, null, null, null, null);
        return equipoService.buscarEquipo(filtros);
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

    public List<EquipoDTO> listarEquipos(){
        return equipoService.listarEquipos();
    }


    //funciones para modificar el equipo
    public EquipoDTO modificarHostname(EquipoUpdateDTO in, String hostname){
        validarTexto(hostname, "Ingresar Hostname válido");

        int id = in.id();

        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, hostname, null, null, null, null, null, null, null);
        return equipoService.actualizarEquipo(dto);
    }

    public EquipoDTO modificarIP(EquipoUpdateDTO in, String ip){
        validarTexto(ip, "Ingresar una IP válida");

        int id = in.id();

        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, null, ip, null, null, null, null, null);
        return equipoService.actualizarEquipo(dto);
    }

    public EquipoDTO modificarModeloCPU(EquipoUpdateDTO in, String modeloCPU){
        validarTexto(modeloCPU, "Ingresar modelo de CPU válido");

        int id = in.id();

        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, null, null, modeloCPU, null, null, null, null);
        return equipoService.actualizarEquipo(dto);
    }

    public EquipoDTO modificarNucleosCPU(EquipoUpdateDTO in, String NucleosCPU){
        validarTexto(NucleosCPU, "Ingresar nucleos de CPU válido");

        int id = in.id();

        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, null, null, null, NucleosCPU, null, null, null);
        return equipoService.actualizarEquipo(dto);
    }

    public EquipoDTO modificarRamTotal(EquipoUpdateDTO in, String ramTotal){
        validarTexto(ramTotal, "Ingresar total de ram válida");

        int id = in.id();

        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, null, null, null, null, ramTotal, null, null);
        return equipoService.actualizarEquipo(dto);
    }

    public EquipoDTO modificarAlmacenamiento(EquipoUpdateDTO in, String almacenamiento){
        validarTexto(almacenamiento, "Ingresar almacenamiento válido");

        int id = in.id();

        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, null, null, null, null, null, almacenamiento, null);
        return equipoService.actualizarEquipo(dto);
    }

    public EquipoDTO modificarModeloGPU(EquipoUpdateDTO in, String modeloGPU){
        validarTexto(modeloGPU, "Ingresar almacenamiento válido");

        int id = in.id();

        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, null, null, null, null, null, null, modeloGPU);
        return equipoService.actualizarEquipo(dto);
    }


    //funciones para cambiar el estado del equipo (operativo, disponible y fuera de uso), se agrega una función opcional por si hay que cambiar a otro estado
    public EquipoDTO deshabilitarEquipo(EquipoUpdateDTO in){
        validarNoNulo(in, "Usuario invalido");
        validarPositivo(in.id(),"ID invalido");

        int id = in.id();
        String estado = "fuera de servicio"; //revisar el estado
        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, estado, null, null, null, null, null, null);
        return equipoService.actualizarEquipo(dto);
    }

    public EquipoDTO habilitarEquipo(EquipoUpdateDTO in){
        validarNoNulo(in, "Equipo invalido");
        validarPositivo(in.id(),"ID invalido");

        int id = in.id();
        String estado = "disponible"; //revisar el estado
        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, estado, null, null, null, null, null, null);
        return equipoService.actualizarEquipo(dto);
    }


    public EquipoDTO equipoEnUso(EquipoUpdateDTO in){
        validarNoNulo(in, "Equipo invalido");
        validarPositivo(in.id(),"ID invalido");

        int id = in.id();
        String estado = "operativo"; //revisar el estado
        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, estado, null, null, null, null, null, null);
        return equipoService.actualizarEquipo(dto);
    }



    //función por si es que hay que cambiar a mas de un estado (opcional, borrable)
    public EquipoDTO modificarEstado(EquipoUpdateDTO in, String estado){
        validarTexto(estado, "Ingresar estado válido");

        int id = in.id();

        EquipoUpdateDTO dto = new EquipoUpdateDTO(id, null, estado, null, null, null, null, null, null);
        return equipoService.actualizarEquipo(dto);
    }


}
