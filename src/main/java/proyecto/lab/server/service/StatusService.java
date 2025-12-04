package proyecto.lab.server.service;

import proyecto.lab.server.dao.ServerStatusDAO;

public class StatusService {
    private final ServerStatusDAO serverStatusDAO;

    public StatusService(ServerStatusDAO serverStatusDAO) {
        this.serverStatusDAO = serverStatusDAO;
    }

    public boolean getStatus(){
        return serverStatusDAO.isServerOnline();
    }
}
