package proyecto.lab.server.dto;
import java.io.Serializable;


public class UsuarioLoginDTO implements Serializable {
    private String rut;
    private String nombre;
    private String contrasena;

    public UsuarioLoginDTO() {}

    // Constructor para logear usuario
    public UsuarioLoginDTO(String rut, String contrasena) {
        this.rut = rut;
        this.contrasena = contrasena;
    }

    // Constructor para registrar Usuario
    public UsuarioLoginDTO(String rut, String nombre, String contrasena) {
        this.rut = rut;
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    //getters
    public String getRut() {
        return rut;
    }

    public String getNombre() { return nombre;}

    public String getContrasena() {
        return contrasena;
    }

    // setters
    public void setRut(String rut) {
        this.rut = rut;
    }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}

