package proyecto.lab.server.models;

import java.sql.Timestamp;

public class Alerta {
    private Integer idAlerta;
    private Integer idEquipo;
    private Integer idTipo;
    private Double valorRegistrado;
    private String mensaje;
    private Timestamp fechaAlerta;

    public Alerta() {
    }

    public Alerta(Integer idAlerta, Integer idEquipo, Integer idTipo, Double valorRegistrado, String mensaje, Timestamp fechaAlerta) {
        this.idAlerta = idAlerta;
        this.idEquipo = idEquipo;
        this.idTipo = idTipo;
        this.valorRegistrado = valorRegistrado;
        this.mensaje = mensaje;
        this.fechaAlerta = fechaAlerta;
    }

    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public Double getValorRegistrado() {
        return valorRegistrado;
    }

    public void setValorRegistrado(Double valorRegistrado) {
        this.valorRegistrado = valorRegistrado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Timestamp getFechaAlerta() {
        return fechaAlerta;
    }

    public void setFechaAlerta(Timestamp fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }
}