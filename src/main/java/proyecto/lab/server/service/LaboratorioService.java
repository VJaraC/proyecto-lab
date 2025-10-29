package proyecto.lab.server.service;
import proyecto.lab.server.dao.LaboratorioDAO;
import proyecto.lab.server.dto.LaboratorioBusquedaDTO;
import proyecto.lab.server.dto.LaboratorioDTO;
import proyecto.lab.server.models.Laboratorio;
import proyecto.lab.server.exceptions.AppException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        //Estado correcto
        String estado = laboratorio.estado_lab().toLowerCase();
        Set<String> estadosPermitidos = Set.of("habilitado", "deshabilitado");
        if(!estadosPermitidos.contains(estado)){
            throw AppException.badRequest("Ingrese un valor válido para estado");
        }

        //Lab ya existente
        int idLab = laboratorio.id_lab();
        Laboratorio laboratorioExistente = laboratorioDAO.BuscarLaboratorioPorId_lab(idLab);
        if(laboratorioExistente != null){
            throw AppException.badRequest("Laboratorio ya existente");
        }
        //Creación nueva lab
        Laboratorio laboratorioNuevo = new Laboratorio(laboratorio);
        boolean insercion = laboratorioDAO.insertarLaboratorio(laboratorioNuevo);
        if(!insercion){
            System.out.println("Error al crear laboratorio");
        }
        return new LaboratorioDTO(laboratorioNuevo); //se le pasa al dto el equipo (modelo) que se creó


    }
    // nombre y todos
    public List<Laboratorio> BuscarLaboratorios(LaboratorioBusquedaDTO filtros) {
        validarNoNulo(filtros, "Datos requeridos");
        List<Laboratorio> resultados = new ArrayList<>();
        return resultados;
    }

}
