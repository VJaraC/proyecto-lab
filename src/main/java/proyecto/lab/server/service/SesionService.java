package proyecto.lab.server.service;

import proyecto.lab.server.dao.SesionDAO;
import proyecto.lab.server.dto.SesionDTO;

public class SesionService {
    private final SesionDAO sesionDAO;

    public SesionService(SesionDAO sesionDAO) {
        this.sesionDAO = sesionDAO;
    }

    public SesionDTO crearSesion(SesionDTO sesion){
        return null;
    }
}
