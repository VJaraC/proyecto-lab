package proyecto.lab.server.service;
import proyecto.lab.server.dao.LaboratorioDAO;
import proyecto.lab.server.dto.LaboratorioBusquedaDTO;
import proyecto.lab.server.dto.LaboratorioDTO;
import proyecto.lab.server.models.Laboratorio;
import proyecto.lab.server.exceptions.AppException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static proyecto.lab.server.utils.ValidadorUtils.validarNoNulo;


public class LaboratorioService {
    private final LaboratorioDAO laboratorioDAO;

    public LaboratorioService(LaboratorioDAO laboratorioDAO) {
        this.laboratorioDAO = laboratorioDAO;
    }

    public LaboratorioDTO crearLaboratorio(LaboratorioDTO laboratorio){
        //DTO vacio
        if(laboratorio == null){
            throw AppException.badRequest("DTO vacio");
        }
        if(laboratorio.id_lab() < 0){
            throw AppException.badRequest("Identificador negativo");
        }
        if (laboratorio.ubicacion() == null || laboratorio.ubicacion().isEmpty()){
            throw AppException.badRequest("El laboratorio debe tener una ubicación");
        }
        if (laboratorio.nombre_lab() == null || laboratorio.nombre_lab().isEmpty()){
            throw AppException.badRequest("El laboratorio debe tener un nombre");
        }
        if(laboratorio.capacidad_personas() < 0 ){
            throw AppException.badRequest("La capacidad de personas tiene que ser positivo");
        }
        if(laboratorio.capacidad_equipo() < 0 ){
            throw AppException.badRequest("La capacidad de equipos tiene que ser positivo");
        }
        if (laboratorio.estado_lab() == null || laboratorio.estado_lab().isEmpty()){
            throw AppException.badRequest("El laboratorio debe tener un estado.");
        }
        if (laboratorio.fecha_registro_lab() == null){
            throw AppException.badRequest("La fecha de registro es obligatoria");
        }

        LocalDate fechaRegistro = laboratorio.fecha_registro_lab();
        LocalDate hoy = LocalDate.now();

        if(fechaRegistro.isAfter(hoy)){
            throw AppException.badRequest("La fecha de registro no puede ser posterior a hoy");
        }

        try{
            Laboratorio existente = laboratorioDAO.buscarLaboratorioPorIdlab(laboratorio.id_lab());
            if(existente != null){throw AppException.badRequest("Laboratorio ya existente");}

            Laboratorio nuevo = new Laboratorio(laboratorio.nombre_lab(), laboratorio.ubicacion(), laboratorio.capacidad_personas(),
                    laboratorio.capacidad_equipo(), laboratorio.estado_lab(), laboratorio.fecha_registro_lab());
            Laboratorio guardado = laboratorioDAO.insertarLaboratorio(nuevo);

            return new LaboratorioDTO(
                    guardado.getId_lab(),
                    guardado.getNombre_lab(),
                    guardado.getUbicacion(),
                    guardado.getCapacidad_personas(),
                    guardado.getCapacidad_equipo(),
                    guardado.getEstado_lab(),
                    guardado.getFecha_registro_lab()
            );
        } catch (AppException e) {
            throw AppException.internal("Error inesperado al crear usuario: " + e.getMessage());
        }


    }
    // nombre y todos
    public List<Laboratorio> BuscarLaboratorios(LaboratorioBusquedaDTO in) {
        validarNoNulo(in, "Datos requeridos");
        final String nombre_lab = in.getNombre_lab() != null ? in.getNombre_lab() : null;
        List<Laboratorio> resultados = new ArrayList<>();
        return resultados;
    }

}
