package proyecto.lab.server.dto;

public class UsuarioBusquedaDTO {
    private Integer id;
    private String nombre;
    private String estado;

    public UsuarioBusquedaDTO(int id, String nombre, String estado) {}

    public UsuarioBusquedaDTO() {}


    //getters

    public Integer getId() { return id; }

    public String getNombre() { return nombre; }

    public String getEstado() { return estado; }

    // setters

    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEstado(String estado) { this.estado = estado; }
}
