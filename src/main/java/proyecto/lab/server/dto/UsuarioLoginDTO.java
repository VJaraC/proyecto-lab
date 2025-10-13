package proyecto.lab.server.dto;
import java.io.Serializable;


public class UsuarioLoginDTO implements Serializable {
    private String nombre;
    private String contrasena;

    public UsuarioLoginDTO() {}

    public UsuarioLoginDTO(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    //getters
    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    // setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}

