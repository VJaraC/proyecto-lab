package proyecto.lab.server.models;
import java.time.LocalDate;
import proyecto.lab.server.dto.LaboratorioDTO;

public class Laboratorio {
    private int id_lab;
    private String nombre_lab;
    private String ubicacion;
    private int capacidad_personas;
    private int capacidad_equipo;
    private String estado_lab;
    private LocalDate fecha_registro_lab;

    public Laboratorio(int id_lab, String nombre_lab, String ubicacion, int capacidad_personas, int capacidad_equipo, String estado_lab, LocalDate fecha_registro_lab){
            this.id_lab = id_lab;
            this.nombre_lab = nombre_lab;
            this.ubicacion = ubicacion;
            this.capacidad_personas = capacidad_personas;
            this.capacidad_equipo = capacidad_equipo;
            this.estado_lab = estado_lab;
            this.fecha_registro_lab = fecha_registro_lab;
    }

    public Laboratorio(String nombre_lab, String ubicacion, int capacidad_personas, int capacidad_equipo, String estado_lab, LocalDate fecha_registro_lab){
        this.nombre_lab = nombre_lab;
        this.ubicacion = ubicacion;
        this.capacidad_personas = capacidad_personas;
        this.capacidad_equipo = capacidad_equipo;
        this.estado_lab = estado_lab;
        this.fecha_registro_lab = fecha_registro_lab;
    }

    // setters
    public void setId_lab(int id_lab){
        this.id_lab = id_lab;
    }

    public void setNombre_lab(String nombre_lab){
        this.nombre_lab = nombre_lab;
    }

    public void setUbicacion(String ubicacion){
        this.ubicacion = ubicacion;
    }

    public void setCapacidad_personas(int capacidad_personas){
        this.capacidad_personas = capacidad_personas;
    }

    public void setCapacidad_equipo(int capacidad_equipo){
        this.capacidad_equipo = capacidad_equipo;
    }

    public void setEstado_lab(String estado_lab){
        this.estado_lab = estado_lab;
    }

    public void setFecha_registro_lab(LocalDate fecha_registro_lab){
        this.fecha_registro_lab = fecha_registro_lab;
    }

    // getters
    public int getId_lab(){
        return id_lab;
    }

    public String getNombre_lab(){
        return nombre_lab;
    }

    public String getUbicacion(){return ubicacion;}

    public int getCapacidad_personas(){
        return capacidad_personas;
    }

    public int getCapacidad_equipo(){
        return capacidad_equipo;
    }

    public String getEstado_lab(){
        return estado_lab;
    }

    public LocalDate getFecha_registro_lab(){
        return fecha_registro_lab;
    }

}
