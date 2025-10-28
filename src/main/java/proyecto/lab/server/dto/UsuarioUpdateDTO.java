package proyecto.lab.server.dto;

import proyecto.lab.server.models.Rol;

import java.io.Serializable;

public class UsuarioUpdateDTO implements Serializable {
    private int id;
    private String nombres;
    private String apellidos;
    private String estado;
    private String email;
    private String telefono;
    private Rol rol;

    public UsuarioUpdateDTO(){}

    public UsuarioUpdateDTO(int id, String nombres, String apellidos, String estado) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estado = estado;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEstado() {
        return estado;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public Rol getRol() {return rol;}

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setRol(Rol rol) {this.rol = rol;}

}
