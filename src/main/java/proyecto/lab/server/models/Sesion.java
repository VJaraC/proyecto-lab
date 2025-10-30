package proyecto.lab.server.models;

import java.time.LocalDate;

public class Sesion {
    private int id_sesion;
    private int id_equipo;
    private String horaInicio;
    private String horaFin;
    private LocalDate fecha;
    private String estado;

    //constructor vacio
    public Sesion() {}

    //constructor
    public Sesion(int id_sesion,
            int id_equipo,
            String horaInicio,
            String horaFin,
            LocalDate fecha,
            String estado){
        this.id_sesion = id_sesion;
        this.id_equipo = id_equipo;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fecha = fecha;
        this.estado = estado;
    }

    //Setters
    public void setId_sesion(int id_sesion) {
        this.id_sesion = id_sesion;
    }
    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }
    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }


    //Getters
    public int getId_sesion() {return id_sesion;}
    public int getId_equipo() {return id_equipo;}
    public String getHoraInicio() {return horaInicio;}
    public String getHoraFin() {return horaFin;}
    public LocalDate getFecha() {return fecha;}
    public String getEstado() {return estado;}

}
