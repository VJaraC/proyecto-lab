package proyecto.lab.server.controller;

import proyecto.lab.server.service.StatusService;

public class StatusController {
    private final StatusService statusService;

    public  StatusController(StatusService statusService){
        this.statusService = statusService;
    }

    public boolean getStatus(){
        return statusService.getStatus();
    }
}
