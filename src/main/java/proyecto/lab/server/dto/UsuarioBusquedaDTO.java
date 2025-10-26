package proyecto.lab.server.dto;

import java.time.LocalDate;

public class UsuarioBusquedaDTO {
    private Integer id;
    private String rut;
    private String nombre;
    private String estado;
    private String genero;
    private String cargo;
    private LocalDate fecha_nacimiento;
    private String telefono;


    public UsuarioBusquedaDTO(int id, String rut, String nombre, String estado) {}

    public UsuarioBusquedaDTO() {}


    //getters
    public int getId() {
        return id;
    }

    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    public String getGenero() {
        return genero;
    }

    public String getCargo() {
        return cargo;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public String getTelefono() {
        return telefono;
    }



    //setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
