package proyecto.lab;

import proyecto.lab.server.controller.EquipoController;
import proyecto.lab.server.controller.UsuarioController;
import proyecto.lab.server.dao.EquipoDAO;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.EquipoBusquedaDTO;
import proyecto.lab.server.dto.EquipoDTO;
import proyecto.lab.server.dto.UsuarioLoginDTO;
import proyecto.lab.server.service.EquipoService;
import proyecto.lab.server.service.UsuarioService;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        /*UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioService usuarioService = new UsuarioService(usuarioDAO);
        UsuarioController usuarioController = new UsuarioController(usuarioService);

        UsuarioLoginDTO nuevousuario = new UsuarioLoginDTO();
        nuevousuario.setNombre("Vito");
        nuevousuario.setRut("21243169-9");
        nuevousuario.setContrasena("vito123");
        usuarioController.crearUsuario(nuevousuario);*/

        EquipoDAO equipoDAO = new EquipoDAO();
        EquipoService equipoService = new EquipoService(equipoDAO);
        EquipoController equipoController = new EquipoController(equipoService);

        EquipoBusquedaDTO filtrosBusqueda = new EquipoBusquedaDTO(1, null, null, null, null,null, null, null,null ,null, null);
        EquipoDTO resultado = equipoController.buscarEquipo(filtrosBusqueda);
        System.out.println(resultado);
    }
}
