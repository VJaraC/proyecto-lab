package proyecto.lab.server.dto;
import proyecto.lab.server.models.Rol;
import java.io.Serializable;
import java.time.LocalDate;


public class UsuarioDTO implements Serializable {
    private int ID;
    private String rut;
    private String nombres;
    private String apellidos;
    private String email;
    private String estado;
    private String genero;
    private String cargo;
    private Rol rol;
    private LocalDate fechaNacimiento;
    private String telefono;

    public UsuarioDTO() {} //Constructor vacío requerido para serialización.

    public UsuarioDTO(int ID, String rut, String nombres, String apellidos, String email, String estado, String genero, String cargo, Rol rol, LocalDate fechaNacimiento, String telefono) {
        this.ID = ID;
        this.rut = rut;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.estado = estado;
        this.genero = genero;
        this.cargo = cargo;
        this.rol = rol;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    public UsuarioDTO( String nombre, String estado) {
        this.nombres = nombre;
        this.estado = estado;
    }

    //getters
    public int getID() {
        return ID;
    }

    public String getRut() {return rut;}

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getEstado() {
        return estado;
    }

    public String getGenero() {
        return genero;
    }

    public String getCargo() {
        return cargo;
    }

    public Rol getRol() { return rol; }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }


    // setters
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setRut(String rut) {this.rut = rut;}
    public void setNombre(String nombres) {
        this.nombres = nombres;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public void setRol(Rol rol) {this.rol = rol;}
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
