package proyecto.lab.server.models;

import java.sql.Timestamp;

public class Sesion {
    private Long idSesion;
    private Integer idEquipo;
    private Integer idUsuario;
    private Timestamp fechaInicio;
    private Timestamp fechaTermino;
    private String estadoSesion;

    public Sesion() {
    }

    public Sesion(Long idSesion, Integer idEquipo, Integer idUsuario, Timestamp fechaInicio, Timestamp fechaTermino, String estadoSesion) {
        this.idSesion = idSesion;
        this.idEquipo = idEquipo;
        this.idUsuario = idUsuario;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.estadoSesion = estadoSesion;
    }

    public Long getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(Long idSesion) {
        this.idSesion = idSesion;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Timestamp fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getEstadoSesion() {
        return estadoSesion;
    }

    public void setEstadoSesion(String estadoSesion) {
        this.estadoSesion = estadoSesion;
    }
}