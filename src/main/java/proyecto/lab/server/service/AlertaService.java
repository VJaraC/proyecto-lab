package proyecto.lab.server.service;

import proyecto.lab.server.dao.AlertaDAO;
import proyecto.lab.server.dto.AlertaDetalleDTO;

public class AlertaService {

    private final AlertaDAO alertaDao;

    public AlertaService(AlertaDAO alertaDao) {
        this.alertaDao = alertaDao;
    }

    public int contarAlertasRecientes() {
        return alertaDao.contarAlertasUltimaHora();
    }

    public AlertaDetalleDTO obtenerUltimaAlerta() {
        return alertaDao.obtenerUltimaAlertaDetallada();
    }
}