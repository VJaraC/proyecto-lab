package proyecto.lab.server.dto;

import java.time.LocalDate;

public class UsuarioBusquedaDTO {
    private Integer id;
    private String rut;
    private String nombre;
    private String apellidos;
    private String estado;
    private String genero;
    private String cargo;
    private LocalDate fecha_nac;
    private String telefono;


    public UsuarioBusquedaDTO(int id, String rut, String nombre, String apellidos, String estado, String genero, String cargo, LocalDate fecha_nac, String telefono) {}

    public UsuarioBusquedaDTO() {}

    //getters
    public Integer getId() {
        return id;
    }

    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {return apellidos;}

    public String getEstado() {
        return estado;
    }

    public String getGenero() {
        return genero;
    }

    public String getCargo() {
        return cargo;
    }

    public LocalDate getFecha_nac() {
        return fecha_nac;
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

    public void setApellidos(String apellidos) { this.apellidos = apellidos;}

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setFecha_nac(LocalDate fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
