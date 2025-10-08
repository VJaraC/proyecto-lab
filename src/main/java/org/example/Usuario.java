package org.example;

public class Usuario {
    //atributos
    private int ID;
    private String nombre;
    private String password;

    //constructor
    public Usuario(int ID, String nombre, String password) {
        this.ID = ID;
        this.nombre = nombre;
        this.password = password;
    }

    //setters
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //getters
    public int getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

}
