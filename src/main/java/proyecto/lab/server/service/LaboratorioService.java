package proyecto.lab.server.service;
import proyecto.lab.server.dao.LaboratorioDAO;
import proyecto.lab.server.dto.LaboratorioBusquedaDTO;
import proyecto.lab.server.dto.LaboratorioDTO;
import proyecto.lab.server.dto.LaboratorioUpdateDTO;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Laboratorio;
import proyecto.lab.server.utils.EstadoUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LaboratorioService {
    private final LaboratorioDAO laboratorioDAO;

    public LaboratorioService(LaboratorioDAO laboratorioDAO) {
        this.laboratorioDAO = laboratorioDAO;
    }

    public LaboratorioDTO crearLaboratorio(LaboratorioDTO lab) {
        if ((lab.id_lab() != null && lab.id_lab() > 0)) {
            throw AppException.badRequest("No debes enviar ID al crear el laboratorio"); // validaciones
        }
        if(lab.nombre_lab() == null || lab.nombre_lab().trim().isEmpty()) {
            throw AppException.badRequest("El nombre del laboratorio es obligatorio");
        }
        if(lab.nombre_lab().length() > 100) {
            throw AppException.badRequest("El nombre del laboratorio no puede superar 100 caracteres");
        }
        if (lab.ubicacion() == null || lab.ubicacion().isEmpty() || lab.ubicacion().length() < 4) {
            throw AppException.badRequest("La ubicación debe tener al menos 4 caracteres.");// validaciones
        }
        if (lab.capacidad_personas() <= 0 || lab.capacidad_personas() > 30) {
            throw AppException.badRequest("La capacidad de personas debe estar entre 1 y 30.");
        }
        if ( lab.capacidad_equipo() <= 0 || lab.capacidad_equipo() > 30) {
            throw AppException.badRequest("La capacidad de equipos debe estar entre 1 y 30.");
        }

        try {
            // crear nuevo laboratorio
            Laboratorio nuevo = new Laboratorio(lab.nombre_lab().trim(),
                    lab.ubicacion().trim(),
                    lab.capacidad_personas(),
                    lab.capacidad_equipo(),
                    EstadoUtils.HABILITADO,
                    LocalDate.now()
            );
            Laboratorio guardado = laboratorioDAO.insertarLaboratorio(nuevo);

            return toDTO(guardado);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw AppException.internal("Error inesperado al crear laboratorio.");
        }

    }

    public LaboratorioDTO buscarLaboratorioPorId(LaboratorioBusquedaDTO in){
        if (in == null ) throw AppException.badRequest("Solicitud inválida.");
        Laboratorio u = requireById(in.getId_lab());
        return toDTO(u);
    }

    public List<LaboratorioDTO> buscarLaboratorios(LaboratorioBusquedaDTO in) {
        if (in == null) throw AppException.badRequest("Solicitud inválida.");

        boolean tieneFiltro =
                (in.getNombre_lab() != null && !in.getNombre_lab().isBlank()) ||
                        (in.getEstado_lab() != null && !in.getEstado_lab().isBlank()) ||
                        (in.getUbicacion() != null && !in.getUbicacion().isBlank()) ||
                        (in.getCapacidad_equipo() > 0 ) ||
                        (in.getCapacidad_personas() > 0 ) ||
                        (in.getFecha_registro_lab() != null);

        if (!tieneFiltro)
            throw AppException.badRequest("Debes especificar al menos un filtro.");

        List<Laboratorio> laboratorios = laboratorioDAO.buscarLaboratorios(
                in.getNombre_lab(),
                in.getUbicacion(),
                in.getCapacidad_personas(),
                in.getCapacidad_equipo(),
                in.getEstado_lab(),
                in.getFecha_registro_lab()
        );

        if (laboratorios.isEmpty()) {
            throw AppException.notFound("No se encontraron laboratorios con los filtros indicados.");
        }

        return toDTOs(laboratorios);
    }



    public List<LaboratorioDTO> listarLaboratorios() {
        try{
            List<Laboratorio> laboratorios = laboratorioDAO.mostrarLaboratorios();
            List<LaboratorioDTO> listaLaboratorios = new ArrayList<>();

            for (Laboratorio u : laboratorios) {
                listaLaboratorios.add(toDTO(u));
            }
            return listaLaboratorios;
        } catch (AppException e){
            throw e;
        } catch(Exception e) {
            throw AppException.internal("Error inesperado al listar laboratorios.");
        }
    }

    public LaboratorioDTO actualizarLaboratorio(LaboratorioUpdateDTO dto) {
        try{
            Laboratorio existente = requireById(dto.id_lab());

            boolean cambios = false;

            if (applyTextChange(dto.nombre_lab(), existente.getNombre_lab(), existente::setNombre_lab, "nombre del laboratorio", true, true)) cambios = true;
            if (applyTextChange(dto.ubicacion(), existente.getUbicacion(), existente::setUbicacion, "ubicación", true, true)) cambios = true;
            if (applyIntChange(dto.capacidad_personas(), existente.getCapacidad_personas(), existente::setCapacidad_personas, "capacidad de personas", true, true)) cambios = true;
            if (applyIntChange(dto.capacidad_equipo(), existente.getCapacidad_equipo(), existente::setCapacidad_equipo, "capacidad de equipos", true, true)) cambios = true;
            if (applyEstadoChange(dto.estado_lab(), existente)) cambios = true;

            if(!cambios){
                throw AppException.badRequest("No hay cambios para aplicar");
            }

            boolean ok = laboratorioDAO.actualizarLaboratorio(existente);

            if(!ok){ throw AppException.internal("Error inesperado al actualizar laboratorio.");}

            return toDTO(existente);
        } catch (AppException e) {
            throw e;
        } catch(Exception e) {
            throw AppException.internal("Error inesperado al actualizar laboratorio.");
        }
    }

    private LaboratorioDTO toDTO(Laboratorio u){
        return new LaboratorioDTO(
                u.getId_lab(),
                u.getNombre_lab(),
                u.getUbicacion(),
                u.getCapacidad_personas(),
                u.getCapacidad_equipo(),
                u.getEstado_lab(),
                u.getFecha_registro_lab()
        );
    }

    private List<LaboratorioDTO> toDTOs(List<Laboratorio> laboratorios){
        return laboratorios.stream().map(this::toDTO).toList();
    }


    private Laboratorio requireById(Integer id) {
        if (id == null || id <= 0) {
            throw AppException.badRequest("Debe proporcionar un ID válido.");
        }
        Laboratorio u = laboratorioDAO.buscarLaboratorioPorID(id);
        if (u == null) throw AppException.notFound("Laboratorio no encontrado.");
        return u;
    }

    private boolean applyTextChange(String nuevo, String actual, java.util.function.Consumer<String> setter,
                                    String etiquetaCampo, boolean rejectEmpty, boolean conflictIfSame) {
        if (nuevo == null) return false;
        String v = nuevo.trim();

        if (rejectEmpty && v.isEmpty()) {
            throw AppException.badRequest("El " + etiquetaCampo + " no puede estar vacío");
        }
        if (conflictIfSame && actual != null && v.equals(actual.trim())) {
            throw AppException.conflict("El " + etiquetaCampo + " ya está asignado en el sistema.");
        }
        setter.accept(v);
        return true;
    }

    private boolean applyIntChange(Integer nuevo, Integer actual, java.util.function.Consumer<Integer> setter,
                                   String etiquetaCampo, boolean rejectZero, boolean conflictIfSame) {
        if (nuevo == null) return false; // si no se envió, no hay cambio

        int valor = nuevo;

        if (rejectZero && valor <= 0) {
            throw AppException.badRequest("El " + etiquetaCampo + " debe ser mayor que cero.");
        }

        if (conflictIfSame && actual != null && valor == actual) {
            throw AppException.conflict("El " + etiquetaCampo + " ya está asignado en el sistema.");
        }

        setter.accept(valor);
        return true;
    }

    private boolean applyEstadoChange(String estadoDto, Laboratorio existente) {
        if (estadoDto == null) return false;

        String nuevoEstado = EstadoUtils.normalizar(estadoDto);
        if (nuevoEstado.isBlank()) {
            throw AppException.badRequest("El estado no puede estar vacío");
        }
        if (!EstadoUtils.esValido(nuevoEstado)) {
            throw AppException.badRequest("El estado debe ser 'habilitado' o 'deshabilitado'");
        }
        if (nuevoEstado.equalsIgnoreCase(existente.getEstado_lab())) {
            throw AppException.conflict("El estado ya está asignado en el sistema.");
        }
        existente.setEstado_lab(nuevoEstado);
        return true;
    }


}
