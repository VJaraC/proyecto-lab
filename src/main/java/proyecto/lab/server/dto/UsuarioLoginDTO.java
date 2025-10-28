package proyecto.lab.server.dto;
import java.io.Serializable;
import java.time.LocalDate;


public class UsuarioLoginDTO implements Serializable {
    private String rut;
    private String nombres;
    private String apellidos;
    private String email;
    private String estado;
    private String genero;
    private String contrasena;
    private String cargo;
    private LocalDate fecha_nacimiento;
    private String telefono;

    public UsuarioLoginDTO() {}

    // Constructor para logear usuario
    public UsuarioLoginDTO(String rut, String contrasena) {
        this.rut = rut;
        this.contrasena = contrasena;
    }

    // Constructor para registrar Usuario
    public UsuarioLoginDTO(String rut, String nombres, String apellidos,  String genero, String contrasena, String cargo, LocalDate fecha_nacimiento, String telefono, String email) {
        this.rut = rut;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.genero = genero;
        this.contrasena = contrasena;
        this.cargo = cargo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.telefono = telefono;
        this.email = email;
    }

    //getters
    public String getRut() {
        return rut;
    }

    public String getNombre() { return nombres;}

    public String getApellidos() { return apellidos;}

    public String getEmail() { return email;}

    public String getGenero() { return genero;}

    public String getContrasena() {
        return contrasena;
    }

    public String getCargo() { return cargo;}

    public LocalDate getFecha_nacimiento() { return fecha_nacimiento;}

    public String getTelefono() { return telefono;}

    // setters
    public void setRut(String rut) {
        this.rut = rut;
    }

    public void setNombre(String nombre) { this.nombres = nombre; }

    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public void setEmail(String email) { this.email = email; }

    public void setGenero(String genero) { this.genero = genero; }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setCargo(String cargo) { this.cargo = cargo; }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) { this.fecha_nacimiento = fecha_nacimiento; }

    public void setTelefono(String telefono) { this.telefono = telefono; }
}

