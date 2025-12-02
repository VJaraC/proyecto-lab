package proyecto.lab.server.controller;
import proyecto.lab.server.dto.*;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.security.AuthUtils;
import proyecto.lab.server.service.LaboratorioService;
import proyecto.lab.server.utils.EstadoUtils;
import java.time.LocalDate;
import java.util.List;
import static proyecto.lab.server.utils.ValidadorUtils.*;

public class LaboratorioController {
    private final LaboratorioService laboratorioService;
    public LaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }
    // =================== Comandos (ADMIN) ===================

    public LaboratorioDTO crearLaboratorio(LaboratorioDTO in, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        validarNoNulo(in, "Datos requeridos");
        validarTexto(in.nombre_lab(), "El nombre del laboratorio es obligatorio");
        validarTexto(in.ubicacion(), "La ubicación es obligatoria");
        if (in.capacidad_personas() <= 0 || in.capacidad_personas() > 30) {
            throw AppException.badRequest("La capacidad de personas debe estar entre 1 y 30.");
        }
        if (in.capacidad_equipo() <= 0 || in.capacidad_equipo() > 30) {
            throw AppException.badRequest("La capacidad de equipos debe estar entre 1 y 30.");
        }
        return laboratorioService.crearLaboratorio(in);
    }

    public LaboratorioDTO modificarNombreLaboratorio(LaboratorioUpdateDTO in , String nuevonombre, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        validarNoNulo(in, "Datos requeridos");
        validarTexto(nuevonombre, "Debes ingresar un nombre");
        validarPositivo(in.id_lab(), "ID invalido");
        int id = in.id_lab();

        LaboratorioUpdateDTO dto = new LaboratorioUpdateDTO(id, nuevonombre, null, null, null, null);

        return laboratorioService.actualizarLaboratorio(dto);
    }
    public LaboratorioDTO modificarUbicacionLaboratorio(LaboratorioUpdateDTO in , String nuevaubicacion, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        validarNoNulo(in, "Datos requeridos");
        validarPositivo(in.id_lab(), "ID invalido");
        validarTexto(nuevaubicacion, "Debes ingresar una nueva ubicación.");
        int id = in.id_lab();

        LaboratorioUpdateDTO dto = new LaboratorioUpdateDTO(id, null, nuevaubicacion, null, null, null );

        return laboratorioService.actualizarLaboratorio(dto);
    }

    public LaboratorioDTO modificarCapacidadPersonas(LaboratorioUpdateDTO in , int nuevacapacidad, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        validarNoNulo(in, "Datos requeridos");
        validarPositivo(in.id_lab(), "ID invalido");
        int id = in.id_lab();
        if (nuevacapacidad <= 0 || nuevacapacidad > 30) {
            throw AppException.badRequest("La capacidad de personas debe estar entre 1 y 30.");
        }

        LaboratorioUpdateDTO dto = new LaboratorioUpdateDTO(id, null, null, nuevacapacidad, null, null);

        return laboratorioService.actualizarLaboratorio(dto);
    }

    public LaboratorioDTO modificarCapacidadEquipos(LaboratorioUpdateDTO in , int nuevacapacidad, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        validarNoNulo(in, "Datos requeridos");
        validarPositivo(in.id_lab(), "ID invalido");
        int id = in.id_lab();
        if ( nuevacapacidad <= 0 || nuevacapacidad > 30) {
            throw AppException.badRequest("La capacidad de equipos debe estar entre 1 y 30.");
        }

        LaboratorioUpdateDTO dto = new LaboratorioUpdateDTO(id, null, null, null, nuevacapacidad, null);

        return laboratorioService.actualizarLaboratorio(dto);
    }

    public LaboratorioDTO habilitarLaboratorio(LaboratorioUpdateDTO in, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        validarNoNulo(in, "Datos requeridos");
        validarPositivo(in.id_lab(), "ID invalido");
        int id = in.id_lab();

        String estado = EstadoUtils.HABILITADO;

        LaboratorioUpdateDTO dto = new LaboratorioUpdateDTO(id, null, null, null, null, estado);
        return laboratorioService.actualizarLaboratorio(dto);
    }

    public LaboratorioDTO deshabilitarLaboratorio(LaboratorioUpdateDTO in, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN);
        validarNoNulo(in, "Datos requeridos");
        validarPositivo(in.id_lab(),"ID invalido");
        int id = in.id_lab();
        String estado = EstadoUtils.DESHABILITADO;

        LaboratorioUpdateDTO dto = new LaboratorioUpdateDTO(id, null, null, null, null, estado);
        return laboratorioService.actualizarLaboratorio(dto);
    }

    public LaboratorioDTO buscarLaboratorioPorId(Integer id, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        validarNoNulo(id, "El id es obligatorio");
        validarPositivo(id, "ID invalido");

        LaboratorioBusquedaDTO filtros = new LaboratorioBusquedaDTO();
        filtros.setId_lab(id);
        return laboratorioService.buscarLaboratorioPorId(filtros);
    }

    public List<LaboratorioDTO> buscarLaboratorioPorNombre(String nombre, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        validarTexto(nombre, "El nombre es obligatorio");
        LaboratorioBusquedaDTO filtros = new LaboratorioBusquedaDTO();
        filtros.setNombre_lab(nombre);
        return laboratorioService.buscarLaboratorios(filtros);
    }

    public List<LaboratorioDTO> buscarLaboratoriosPorUbicacion(String ubicacion, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        validarTexto(ubicacion, "La ubicación es obligatoria");

        LaboratorioBusquedaDTO filtros = new LaboratorioBusquedaDTO();
        filtros.setUbicacion(ubicacion);
        return laboratorioService.buscarLaboratorios(filtros);
    }

    public List<LaboratorioDTO> buscarLaboratoriosPorEstado(String estado, UsuarioDTO auth){
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        validarNoNulo(estado, "El estado es obligatorio");
        validarEstado(estado, "El estado debe ser habilitado o deshabilitado");

        LaboratorioBusquedaDTO filtros = new LaboratorioBusquedaDTO();
        filtros.setEstado_lab(estado);
        return laboratorioService.buscarLaboratorios(filtros);
    }

    public List<LaboratorioDTO> buscarLaboratoriosPorCapacidades(Integer capPersonas, Integer capEquipos, UsuarioDTO auth) {
        AuthUtils.requireRole(auth, Rol.ADMIN, Rol.MONITOR);
        LaboratorioBusquedaDTO filtros = new LaboratorioBusquedaDTO();
        if (capPersonas != null) {
            if (capPersonas <= 0) throw AppException.badRequest("capacidad de personas inválida");
            filtros.setCapacidad_personas(capPersonas);
        }
        if (capEquipos != null) {
            if (capEquipos <= 0) throw AppException.badRequest("capacidad de equipos inválida");
            filtros.setCapacidad_equipo(capEquipos);
        }
        if (filtros.getCapacidad_personas() == null && filtros.getCapacidad_equipo() == null) {
            throw AppException.badRequest("Debes especificar al menos una capacidad.");
        }

        return laboratorioService.buscarLaboratorios(filtros);
    }
    public List<LaboratorioDTO> buscarLaboratoriosPorFecha(LocalDate fecha, UsuarioDTO auth) {
        AuthUtils.requireRole(auth, Rol.ADMIN,Rol.MONITOR);
        validarNoNulo(fecha, "La fecha es obligatoria");

        LaboratorioBusquedaDTO filtros = new LaboratorioBusquedaDTO();
        filtros.setFecha_registro_lab(fecha);
        return laboratorioService.buscarLaboratorios(filtros);
    }

    public List<LaboratorioDTO> listarLaboratorios(UsuarioDTO auth) {
        AuthUtils.requireRole(auth, Rol.ADMIN,Rol.MONITOR);
        return laboratorioService.listarLaboratorios();
    }

    public int contarLaboratoriosSesionActiva(){
        return laboratorioService.contarLaboratoriosSesionActiva();
    }

    public int contarLaboratoriosActivos(){
        return laboratorioService.contarLaboratoriosActivos();
    }
}
