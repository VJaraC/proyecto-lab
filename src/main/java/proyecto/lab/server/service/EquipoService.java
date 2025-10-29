package proyecto.lab.server.service;

import proyecto.lab.server.dao.EquipoDAO;
import proyecto.lab.server.dto.EquipoBusquedaDTO;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.EquipoUpdateDTO;
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

    //función para hacer la busqueda de mas de un equipo.
    public List<EquipoDTO> buscarEquipos(EquipoBusquedaDTO filtros){
        validarNoNulo(filtros, "Datos requeridos");
        List<Equipo> resultados = new ArrayList<>();

        if(filtros.id_admin() != null){
            resultados = equipoDAO.buscarPorIdAdmin(filtros.id_admin());
        }
        else if(filtros.id_lab_equipo() != null){
            resultados = equipoDAO.buscarEquipoPorIdLab(filtros.id_lab_equipo());
        }
        else if(filtros.hostname() != null){
            resultados = equipoDAO.buscarEquipoPorHostname(filtros.hostname());
        }
        else if(filtros.fabricante() != null){
            resultados = equipoDAO.buscarPorFabricante(filtros.fabricante());
        }
        else if(filtros.estado() != null){
            resultados = equipoDAO.buscarPorEstado(filtros.estado());
        }
        else if(filtros.modelo() != null){
            resultados = equipoDAO.buscarEquipoPorModelo(filtros.modelo());
        }
        else if(filtros.mac() != null){
            resultados = equipoDAO.buscarPorMac(filtros.mac());
        }
        else if(filtros.ip() != null){
            resultados = equipoDAO .buscarPorIp(filtros.ip());
        }
        else if(filtros.fecha_ingreso() != null){
            resultados = equipoDAO.buscarPorFechaIngreso(filtros.fecha_ingreso());
        }

        //transforma la lista de Equipo a EquipoDTO
        return resultados.stream()
                .map(EquipoDTO::new)
                .toList();
    }

    //funcion para hacer la busqueda de un solo equipo (id, numero de serie)
    public EquipoDTO buscarEquipo(EquipoBusquedaDTO filtros){
        validarNoNulo(filtros, "Datos requeridos");
        Equipo equipo = new Equipo();
        if(filtros.id_equipo() != null){
            equipo = equipoDAO.buscarEquipoPorId(filtros.id_equipo());
        }
        else if(filtros.numero_serie() != null){
            equipo  = equipoDAO.buscarEquipoPorNumSerie(filtros.numero_serie());
        }
        else{
            System.out.println("Error al buscar equipo");
        }
        return new EquipoDTO(equipo);
    }

    public EquipoDTO actualizarEquipo(EquipoUpdateDTO dto) {
        Equipo existente = equipoDAO.buscarEquipoPorId(dto.id());

        if(existente == null){
            throw AppException.badRequest("Equipo no existe");
        }

        if(dto.hostname() != null){
            String nuevoHostname = dto.hostname().trim();
            if(nuevoHostname.equals(existente.getHostname().trim())){
                throw AppException.badRequest("Hostname ya existente");
            }
            existente.setHostname(nuevoHostname);
        }

        if(dto.estado() != null){
            String nuevoEstado = dto.estado().trim();
            if(nuevoEstado.equals(existente.getEstado())){
                throw AppException.badRequest("Estado ya existente");
            }
            existente.setEstado(nuevoEstado);
        }
        if(dto.ip() != null){
            String nuevoIp = dto.ip().trim();
            if(nuevoIp.equals(existente.getIp())){
                throw AppException.badRequest("Ip ya existente");
            }
            existente.setIp(nuevoIp);
        }
        if(dto.modeloCPU() != null){
            String nuevoModeloCPU = dto.modeloCPU().trim();
            if(nuevoModeloCPU.equals(existente.getModeloCPU())){
                throw AppException.badRequest("ModeloCPU ya existente");
            }
            existente.setModeloCPU(nuevoModeloCPU);
        }
        if(dto.nucleosCPU() != null){
            String nuevoNucleos = dto.nucleosCPU().trim();
            if(nuevoNucleos.equals(existente.getNucleosCPU())){
                throw AppException.badRequest("Nucleos CPU ya existente");
            }
            existente.setNucleosCPU(nuevoNucleos);
        }
        if(dto.ramTotal() != null){
            String nuevoRamTotal = dto.ramTotal().trim();
            if(nuevoRamTotal.equals(existente.getRamTotal())){
                throw AppException.badRequest("Ram total ya existente");
            }
            existente.setRamTotal(nuevoRamTotal);
        }
        if(dto.almacenamiento() != null){
            String nuevoAlmacenamiento = dto.almacenamiento().trim();
            if(nuevoAlmacenamiento.equals(existente.getAlmacenamiento())){
                throw AppException.badRequest("Almacenamiento ya existente");
            }
            existente.setAlmacenamiento(nuevoAlmacenamiento);
        }
        if(dto.modeloGPU() != null){
            String nuevoModeloGPU = dto.modeloGPU().trim();
            if(nuevoModeloGPU.equals(existente.getModeloGPU())){
                throw AppException.badRequest("ModeloGPU ya existente");
            }
            existente.setModeloGPU(nuevoModeloGPU);
        }

        Boolean ok = equipoDAO.actualizarEquipo(existente);
        if(!ok){
            throw AppException.badRequest("Error al actualizar equipo");
        }
        return new EquipoDTO(existente);

    }
}
