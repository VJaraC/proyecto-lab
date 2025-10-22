package proyecto.lab.server.dto;
import java.io.Serializable;


public class UsuarioDTO implements Serializable {
    private int ID;
    private String rut;
    private String nombre;
    private String estado;

    public UsuarioDTO() {} //Constructor vacío requerido para serialización.

    public UsuarioDTO(int ID, String rut, String nombre, String estado) {
        this.ID = ID;
        this.rut = rut;
        this.nombre = nombre;
        this.estado = estado;
    }

    public UsuarioDTO( String nombre, String estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    //getters
    public int getID() {
        return ID;
    }

    public String getRut() {return rut;}

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    // setters
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setRut(String rut) {this.rut = rut;}
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
