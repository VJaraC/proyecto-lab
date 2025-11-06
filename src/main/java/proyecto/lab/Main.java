package proyecto.lab;

import proyecto.lab.server.controller.EquipoController;
import proyecto.lab.server.controller.LaboratorioController;
import proyecto.lab.server.controller.UsuarioController;
import proyecto.lab.server.dao.EquipoDAO;
import proyecto.lab.server.dao.LaboratorioDAO;
import proyecto.lab.server.dao.UsuarioDAO;
import proyecto.lab.server.dto.*;
import proyecto.lab.server.models.Rol;
import proyecto.lab.server.service.EquipoService;
import proyecto.lab.server.service.LaboratorioService;
import proyecto.lab.server.service.UsuarioService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        UsuarioService usuarioService =  new UsuarioService(usuarioDAO);
        UsuarioController usuarioController = new UsuarioController(usuarioService);

        EquipoDAO equipoDAO = new EquipoDAO();
        EquipoService equipoService = new EquipoService(equipoDAO);
        EquipoController equipoController = new EquipoController(equipoService);

        LaboratorioDAO laboratorioDAO = new LaboratorioDAO();
        LaboratorioService laboratorioService = new LaboratorioService(laboratorioDAO);
        LaboratorioController  laboratorioController = new LaboratorioController(laboratorioService);

        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Servidor escuchando en el puerto: " +  serverSocket.getLocalPort());
        while (true) {
            Socket socket = serverSocket.accept();
            handleRequest(socket, usuarioController, equipoController, laboratorioController);

        }

    }
    enum Action { LOGIN, INSERT_METRIC, GET_METRICS, LIST_USERS, LIST_EQUIPOS, UNKNOWN }
    //funcion que maneja las solicitudes
    private static void handleRequest(Socket socket, UsuarioController usuarioController, EquipoController equipoController, LaboratorioController laboratorioController) throws IOException {
        try(socket;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //lee datos que llegan desde el cliente
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) { //envia datos hacia el cliente

            //leer el mensaje
            String payload = leerDesdeSocket(in);
            //detectar la accion para ser enviada posteriormente a su respectivo controlador
            Action action = detectarTipoSolicitud(payload);
            //validad y extraer datos en un DTO de entrada
            Object requestDTO = validarYExtraerDatos(payload, action);

            Object result;
            switch (action) {
                case LOGIN -> result = usuarioController.iniciarSesion((UsuarioLoginDTO) requestDTO);
                default -> result = error("ACCION_DESCONOCIDA", "Acción no soportada");
            }
        }
    }

    private static String leerDesdeSocket(BufferedReader in) throws IOException {
        String line = in.readLine();
        if (line == null || line.isBlank()) throw new IOException();
        return line.trim();
    }

    private static Action detectarTipoSolicitud(String json) {
        //accion que llega desde el json que envia el cliente. El servidor solo la recibe, ahora la está procesando
        if(json.contains("\"action\":\"login\"")) return Action.LOGIN;
        if(json.contains("\"action\":\"list_users\"")) return Action.LIST_USERS;
        return Action.UNKNOWN;
    }

    //funcion para contruir el DTO de entrada
    private static Object validarYExtraerDatos(String json, Action action) {
        switch (action) {
            case LOGIN -> {
                //aqui se deben recuperar los datos del json que llega desde el cliente y ya fue procesado por las demas funcioens (json)
                String rut = "";
                String password = "";
                return new UsuarioLoginDTO(rut, password);
            }
            default -> {
                return null;
            }
        }
    }
    static Map<String,Object> error(String code, String message) {
        Map<String,Object> m = new HashMap<>();
        m.put("errorCode", code);
        m.put("message", message);
        return m;
    }
}

