package proyecto.lab;

import proyecto.lab.server.controller.EquipoController;
import proyecto.lab.server.controller.UsuarioController;
import proyecto.lab.server.dao.EquipoDAO;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.*;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.service.EquipoService;
import proyecto.lab.server.service.UsuarioService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        /*EquipoDAO equipoDAO = new EquipoDAO();
        EquipoService equipoService = new EquipoService(equipoDAO);
        EquipoController equipoController = new EquipoController(equipoService);
        LocalDate fecha = LocalDate.of(1995, 5, 23);
//        EquipoDTO equipoDTO = new EquipoDTO(1, 1, "Equipo_4", "377", "lenovo", "disponible", "lenovo", "333222111", "132.32.1.3", "intel", "8", "1000", "10000", "amd", fecha);
        EquipoUpdateDTO dto = new EquipoUpdateDTO(1, 1, null, null, null, null, null, null, null, null);
        equipoController.cambiarLabEquipo(dto, 2);*/

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioService usuarioService = new UsuarioService(usuarioDAO);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        Rol rol = Rol.valueOf("ADMIN");
        LocalDate fecha = LocalDate.of(2001, 12, 5);
        UsuarioDTO auth = new UsuarioDTO(1,"20.921.970-0","Daniza Michelle","Peso Paredes","pesodaniza@gmail.com","habilitado","femenino","TI",rol,fecha,"971313911");
        UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO("21.243.169-9","Victor Rolando","Jara Cespedes","masculino","vito123","TI",fecha,"964031692","vitoimportante2@gmail.com");
        usuarioController.crearUsuario(usuarioLoginDTO,auth);

        //EquipoBusquedaDTO filtrosBusqueda = new EquipoBusquedaDTO(null, null, null, null, "12341234", null, null, null, null, null, null);
        //List<EquipoDTO> resultados = equipoController.buscarEquipoPorFabricante("Lenovo");
        //EquipoDTO resultado = null; //equipoController.buscarEquipoPorId(1);
        /*if(resultado != null){
            System.out.println("=== Equipo ===");
            System.out.println("ID: " + resultado.id_equipo());
            System.out.println("Nombre laboratorio: " + resultado.nombreLab());
            System.out.println("Nombre: " + resultado.hostname());
            System.out.println("Numero de serie: " + resultado.numero_serie());
            System.out.println("Fabricante: " + resultado.fabricante());
            System.out.println("Estado: " + resultado.estado());
            System.out.println("modelo: " + resultado.modelo());
            System.out.println("-----------------------------");
        }else {
            for (EquipoDTO equipo : resultados) {
                System.out.println("=== Equipo ===");
                System.out.println("ID: " + equipo.id_equipo());
                System.out.println("Nombre laboratorio: " + equipo.nombreLab());
                System.out.println("Nombre: " + equipo.hostname());
                System.out.println("Numero de serie: " + equipo.numero_serie());
                System.out.println("Fabricante: " + equipo.fabricante());
                System.out.println("Estado: " + equipo.estado());
                System.out.println("modelo: " + equipo.modelo());
                System.out.println("-----------------------------");
            }
        }*/




    }
}
