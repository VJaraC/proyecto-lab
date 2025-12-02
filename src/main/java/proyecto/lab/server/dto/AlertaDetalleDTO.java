package proyecto.lab.server.dto; // O tu paquete de DTOs

import java.sql.Timestamp;

public class AlertaDetalleDTO {
    private Integer idAlerta;
    private String hostname;
    private String nombreMetrica;
    private Double valorRegistrado;
    private String mensaje;
    private Timestamp fechaAlerta;

    public AlertaDetalleDTO() {
    }

    public AlertaDetalleDTO(Integer idAlerta, String hostname, String nombreMetrica, Double valorRegistrado, String mensaje, Timestamp fechaAlerta) {
        this.idAlerta = idAlerta;
        this.hostname = hostname;
        this.nombreMetrica = nombreMetrica;
        this.valorRegistrado = valorRegistrado;
        this.mensaje = mensaje;
        this.fechaAlerta = fechaAlerta;
    }

    // Getters y Setters
    public Integer getIdAlerta() { return idAlerta; }
    public void setIdAlerta(Integer idAlerta) { this.idAlerta = idAlerta; }

    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }

    public String getNombreMetrica() { return nombreMetrica; }
    public void setNombreMetrica(String nombreMetrica) { this.nombreMetrica = nombreMetrica; }

    public Double getValorRegistrado() { return valorRegistrado; }
    public void setValorRegistrado(Double valorRegistrado) { this.valorRegistrado = valorRegistrado; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Timestamp getFechaAlerta() { return fechaAlerta; }
    public void setFechaAlerta(Timestamp fechaAlerta) { this.fechaAlerta = fechaAlerta; }
}