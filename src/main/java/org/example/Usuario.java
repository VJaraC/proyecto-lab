package org.example;

public class Usuario {
    //atributos
    private String nombre;
    private String apellido;
    private String rut;
    private String email;
    private String password;
    private String rol;

    // métodos
    //constructor
    public Usuario(String nombre, String apellido, String rut, String email, String password, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
    //getters
    public String getNombre() { return nombre;}
    public String getApellido() { return apellido;}
    public String getRut() { return rut;}
    public String getEmail() { return email;}
    public String getPassword() { return password;}
    public String getRol() { return rol;}

    //setters
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setApellido(String apellido) {this.apellido = apellido;}
    public void setRut(String rut) {this.rut = rut;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setRol(String rol) {this.rol = rol;}
}
