package proyecto.lab.server.models;

public class Usuario {
    //atributos
    private int ID;
    private String nombre;
    private String estado;
    private String contrasena;

    //constructor
    public Usuario(int ID, String nombre, String estado, String contrasena) {
        this.ID = ID;
        this.nombre = nombre;
        this.estado = estado;
        this.contrasena = contrasena;
    }

    public Usuario(String nombre, String estado, String contrasena) {
        this.nombre = nombre;
        this.estado = estado;
        this.contrasena = contrasena;
    }

    public Usuario(int ID ,String nombre, String estado) {
        this.ID = ID;
        this.nombre = nombre;
        this.estado = estado;
    }


    //setters
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    //getters
    public int getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    public String getContrasena() {
        return contrasena;
    }

}
