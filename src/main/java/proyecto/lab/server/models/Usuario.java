package proyecto.lab.server.models;

public class Usuario {
    //atributos
    private int ID;
    private String nombre;
    private String estado;
    private String password;

    //constructor
    public Usuario(int ID, String nombre, String estado, String password) {
        this.ID = ID;
        this.nombre = nombre;
        this.estado = estado;
        this.password = password;
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

    public String getEstado() {
        return estado;
    }

    public String getPassword() {
        return password;
    }

}
