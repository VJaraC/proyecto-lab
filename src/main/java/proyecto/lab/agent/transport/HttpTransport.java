package proyecto.lab.agent.transport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import proyecto.lab.agent.model.BatchPayload;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

public class HttpTransport implements Transport {

    private final String serverUrl;
    private final HttpClient client;
    private final Gson gson;

    public HttpTransport(String serverUrl) {
        this.serverUrl = serverUrl;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.toString())) // Lo convierte a formato ISO-8601 ("2023-11-24T10:15:30Z")
                .create();
    }

    @Override
    public void send(BatchPayload payload) throws Exception {
        // 1. Convertir el objeto BatchPayload a JSON (ahora Instant funcionará bien)
        String jsonBody = gson.toJson(payload);

        // Debug: Ver qué estamos enviando
        // System.out.println("[DEBUG JSON] " + jsonBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        System.out.println("[AgentTransport] Enviando lote (" + payload.samples().size() + " samples) a: " + serverUrl);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Server error: " + response.statusCode() + " Body: " + response.body());
        }

        System.out.println("[AgentTransport] Envío exitoso. Código: " + response.statusCode());
    }
}