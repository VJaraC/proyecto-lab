package proyecto.lab.server.models;
import java.time.LocalDate;

public class Usuario {
    //atributos
    private int ID;
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

    //constructor
    public Usuario(int ID, String rut, String nombres, String apellidos, String email, String estado, String genero, String contrasena, String cargo, LocalDate fecha_nacimiento, String telefono) {
        this.ID = ID;
        this.rut = rut;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.estado = estado;
        this.genero = genero;
        this.contrasena = contrasena;
        this.cargo = cargo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.telefono = telefono;
    }

    //constructor usado en crear usuario
    public Usuario(String rut, String nombres, String apellidos, String estado, String genero, String contrasena, String cargo, LocalDate fecha_nacimiento, String telefono, String email) {
        this.rut = rut;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estado = estado;
        this.genero = genero;
        this.contrasena = contrasena;
        this.cargo = cargo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.telefono = telefono;
        this.email = email;
    }

    public Usuario(int ID, String rut, String nombre, String apellidos, String estado) {
        this.ID = ID;
        this.rut = rut;
        this.nombres = nombre;
        this.apellidos = apellidos;
        this.estado = estado;
    }


    //setters
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setRut(String rut) { this.rut=rut; }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setEmail(String email) { this.email = email; }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }


    //getters
    public int getID() {
        return ID;
    }

    public String getRut() {return rut;}

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() { return apellidos;}

    public String getEmail() {return email;}

    public String getEstado() {
        return estado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCargo() {
        return cargo;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getGenero() {
        return genero;
    }

}
