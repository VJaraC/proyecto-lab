package proyecto.lab.server.dto;

public class UsuarioBusquedaDTO {
    private Integer id;
    private String rut;
    private String nombre;
    private String estado;

    public UsuarioBusquedaDTO(int id, String rut, String nombre, String estado) {}

    public UsuarioBusquedaDTO() {}


    //getters

    public Integer getId() { return id; }

    public String getRut() { return rut; }

    public String getNombre() { return nombre; }

    public String getEstado() { return estado; }

    // setters

    public void setId(Integer id) { this.id = id; }
    public void setRut(String rut) { this.rut = rut; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEstado(String estado) { this.estado = estado; }
}
