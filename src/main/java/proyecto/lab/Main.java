package proyecto.lab;

import proyecto.lab.server.controller.EquipoController;
import proyecto.lab.server.controller.UsuarioController;
import proyecto.lab.server.dao.EquipoDAO;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.EquipoBusquedaDTO;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.UsuarioDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.service.EquipoService;
import proyecto.lab.server.service.UsuarioService;

import java.sql.SQLException;
import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        EquipoDAO equipoDAO = new EquipoDAO();
        EquipoService equipoService = new EquipoService(equipoDAO);
        EquipoController equipoController = new EquipoController(equipoService);

        EquipoBusquedaDTO filtrosBusqueda = new EquipoBusquedaDTO(null, null, null, null, "12341234", null, null, null, null, null, null);
        EquipoDTO resultados = equipoController.buscarEquipo(filtrosBusqueda);
        System.out.println(resultados);

    }
}
