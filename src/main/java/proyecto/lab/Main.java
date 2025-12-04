package proyecto.lab;

import proyecto.lab.server.controller.EquipoController;
import proyecto.lab.server.controller.MetricasController;
import proyecto.lab.server.controller.SesionController;
import proyecto.lab.server.controller.UsuarioController;
import proyecto.lab.server.dao.EquipoDAO;
import proyecto.lab.server.dao.MetricasDAO;
import proyecto.lab.server.dao.SesionDAO;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.*;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.service.EquipoService;
import proyecto.lab.server.service.MetricasService;
import proyecto.lab.server.service.SesionService;
import proyecto.lab.server.service.UsuarioService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {

        MetricasDAO metricasDAO = new MetricasDAO();
        MetricasService metricasService = new MetricasService(metricasDAO);
        MetricasController metricasController = new MetricasController(metricasService);
        System.out.println(metricasController.obtenerCpu("Vito"));


        /*SesionDAO sesionDAO = new SesionDAO();
        SesionService sesionService = new SesionService(sesionDAO);
        SesionController sesionController = new SesionController(sesionService);
        List<SesionHoraDTO> lista = sesionController.obtenerSesionesPorHora();
        System.out.println(lista.get(10));*/


        /*EquipoDAO equipoDAO = new EquipoDAO();
        EquipoService equipoService = new EquipoService(equipoDAO);
        EquipoController equipoController = new EquipoController(equipoService);
        System.out.println(equipoController.contarEquiposActivos());*/


        /*UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioService usuarioService = new UsuarioService(usuarioDAO);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        Rol rol = Rol.valueOf("ADMIN");
        LocalDate fecha = LocalDate.of(2001, 12, 5);
        UsuarioDTO auth = new UsuarioDTO(1,"20.921.970-0","Daniza Michelle","Peso Paredes","pesodaniza@gmail.com","habilitado","femenino","TI",rol,fecha,"971313911");
        UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO("21.243.169-9","Victor Rolando","Jara Cespedes","masculino","vito123","TI",fecha,"964031692","vitoimportante2@gmail.com");
        usuarioController.crearUsuario(usuarioLoginDTO,auth);*/

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
