package proyecto.lab.server.dto;

import java.time.LocalDate;

public class LaboratorioBusquedaDTO {
    private Integer id_lab;
    private String nombre_lab;
    private String ubicacion;
    private Integer capacidad_personas;
    private Integer capacidad_equipo;
    private String estado_lab;
    private LocalDate fecha_registro_lab;

    public LaboratorioBusquedaDTO(int id_lab, String nombre_lab, String ubicacion, Integer capacidad_personas, Integer capacidad_equipo, String estado_lab) {}

    public LaboratorioBusquedaDTO() {}

    // getters
    public int getId_lab() {
        return id_lab;
    }

    public String getNombre_lab() {
        return nombre_lab;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Integer getCapacidad_personas() {
        return capacidad_personas;
    }

    public Integer getCapacidad_equipo() {
        return capacidad_equipo;
    }

    public String getEstado_lab() {
        return estado_lab;
    }

    public LocalDate getFecha_registro_lab() {
        return fecha_registro_lab;
    }

    // setters
    public void setId_lab(Integer id_lab) {
        this.id_lab = id_lab;
    }

    public void setNombre_lab(String nombre_lab) {
        this.nombre_lab = nombre_lab;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setCapacidad_personas(Integer capacidad_personas) {
        this.capacidad_personas = capacidad_personas;
    }

    public void setCapacidad_equipo(Integer capacidad_equipo) {
        this.capacidad_equipo = capacidad_equipo;
    }

    public void setEstado_lab(String estado_lab) {
        this.estado_lab = estado_lab;
    }

    public void setFecha_registro_lab(LocalDate fecha_registro_lab) {
        this.fecha_registro_lab = fecha_registro_lab;
    }
}
