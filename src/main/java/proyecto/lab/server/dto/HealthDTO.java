package proyecto.lab.server.dto; // O tu paquete de modelos cliente

import com.google.gson.annotations.SerializedName;

public class HealthDTO {

    @SerializedName("status")
    private String status;      // "UP" o "DOWN"

    @SerializedName("database")
    private String database;    // "CONNECTED" o "DISCONNECTED"

    @SerializedName("timestamp")
    private long timestamp;

    // Constructor vacío
    public HealthDTO() {}

    // Getters
    public String getStatus() { return status; }
    public String getDatabase() { return database; }
    public long getTimestamp() { return timestamp; }

    // Helper para saber si todo está bien
    public boolean isUp() {
        return "UP".equalsIgnoreCase(status);
    }
}