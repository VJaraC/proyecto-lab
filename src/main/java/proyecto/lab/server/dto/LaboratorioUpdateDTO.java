package proyecto.lab.server.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class LaboratorioUpdateDTO implements Serializable {
    private int id_lab;
    private String nombre_lab;
    private String ubicacion;
    private String estado_lab;

    // Extras opcionales (vía setters)
    private String capacidad_personas;
    private String capacidad_equipo;
    private LocalDate fecha_registro_lab;

    public LaboratorioUpdateDTO(){}

    public LaboratorioUpdateDTO(int id_lab, String nombre_lab, String ubicacion, String estado_lab) {
        this.id_lab = id_lab;
        this.nombre_lab = nombre_lab;
        this.ubicacion = ubicacion;
        this.estado_lab = estado_lab;
    }

    // Getters
    public int getId_lab() {
        return id_lab;
    }

    public String getNombre_lab() {
        return nombre_lab;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getEstado_lab() {
        return estado_lab;
    }

    public String getCapacidad_personas() {
        return capacidad_personas;
    }

    public String getCapacidad_equipo() {
        return capacidad_equipo;
    }

    public LocalDate getFecha_registro_lab() {
        return fecha_registro_lab;
    }

    // Setters
    public void setId_lab(int id_lab) {
        this.id_lab = id_lab;
    }

    public void setNombre_lab(String nombre_lab) {
        this.nombre_lab = nombre_lab;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setEstado_lab(String estado_lab) {
        this.estado_lab = estado_lab;
    }

    public void setCapacidad_personas(String capacidad_personas) {
        this.capacidad_personas = capacidad_personas;
    }

    public void setCapacidad_equipo(String capacidad_equipo) {
        this.capacidad_equipo = capacidad_equipo;
    }

    public void setFecha_registro_lab(LocalDate fecha_registro_lab) {
        this.fecha_registro_lab = fecha_registro_lab;
    }
}
