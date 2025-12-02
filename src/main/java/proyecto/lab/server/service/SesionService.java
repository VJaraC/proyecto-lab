package proyecto.lab.server.service;

import proyecto.lab.server.dao.EquipoDAO;
import proyecto.lab.server.dao.SesionDAO;
import proyecto.lab.server.dto.*;
import proyecto.lab.server.exceptions.AppException;
import proyecto.lab.server.models.Equipo;
import proyecto.lab.server.models.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static proyecto.lab.server.utils.ValidadorUtils.validarNoNulo;

public class SesionService {
    private final SesionDAO sesionDAO;

    public SesionService(SesionDAO sesionDAO) { this.sesionDAO = sesionDAO; }

    public int contarSesionesActivas(){
        return sesionDAO.contarSesionesActivas();
    }

    public int contarSesionesTotales(){
        return sesionDAO.contarSesionesTotales();
    }

    public List<SesionHoraDTO> obtenerSesionesPorHora(){
        return sesionDAO.obtenerSesionesPorHora();
    }
}
