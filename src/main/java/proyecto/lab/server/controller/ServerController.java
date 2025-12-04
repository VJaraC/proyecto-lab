package proyecto.lab.server.controller;

import com.google.gson.Gson;
import proyecto.lab.server.dto.HealthDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ServerController {

    private final HttpClient httpClient;
    private final Gson gson;
    private final String serverUrl;

    public ServerController(String serverUrl) {
        // Configuramos el cliente con un Timeout corto (ej: 3 segundos)
        // Si el servidor no responde en 3s, asumimos que está caído.
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        this.gson = new Gson();
        this.serverUrl = serverUrl;
    }

    /**
     * Consulta el estado del servidor.
     * @return HealthDto con los datos, o null si no hay conexión.
     */
    public HealthDTO verificarEstado() {
        try {
            // 1. Construir la solicitud GET a /health
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/health")) // Asegúrate de que serverUrl no termine en /
                    .header("Accept", "application/json")
                    // Header para Ngrok (por si acaso)
                    .header("ngrok-skip-browser-warning", "true")
                    .GET()
                    .build();

            // 2. Enviar y esperar respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 3. Validar código HTTP (200 OK)
            if (response.statusCode() == 200) {
                // 4. Procesar el JSON y convertirlo a objeto
                return gson.fromJson(response.body(), HealthDTO.class);
            } else {
                System.err.println("Servidor respondió con error: " + response.statusCode());
                return null; // O un DTO con estado DOWN
            }

        } catch (Exception e) {
            // Si hay excepción (timeout, sin internet, server apagado), retornamos null
            System.err.println("No se pudo conectar al servidor: " + e.getMessage());
            return null;
        }
    }
}