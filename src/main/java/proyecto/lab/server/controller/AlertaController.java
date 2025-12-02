package proyecto.lab.server.controller;

import proyecto.lab.server.dto.AlertaDetalleDTO;
import proyecto.lab.server.service.AlertaService;

public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    public int contarAlertasRecientes() {
        return alertaService.contarAlertasRecientes();
    }

    public AlertaDetalleDTO obtenerUltimaAlerta() {
        return alertaService.obtenerUltimaAlerta();
    }
}