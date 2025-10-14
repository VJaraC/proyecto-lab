package proyecto.lab.server.dto;

import java.io.Serializable;

public class UsuarioUpdateDTO implements Serializable {
    private int id;
    private String nombre;
    private String estado;

    public UsuarioUpdateDTO(){}

    public UsuarioUpdateDTO(int id, String nombre, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
