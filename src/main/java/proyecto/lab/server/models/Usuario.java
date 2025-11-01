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
    private Rol rol;
    private LocalDate fecha_nac;
    private String telefono;

    //constructor
    public Usuario(int ID, String rut, String nombres, String apellidos, String email, String estado, String genero, String contrasena, String cargo, Rol rol, LocalDate fecha_nac, String telefono) {
        this.ID = ID;
        this.rut = rut;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.estado = estado;
        this.genero = genero;
        this.contrasena = contrasena;
        this.cargo = cargo;
        this.rol = rol;
        this.fecha_nac = fecha_nac;
        this.telefono = telefono;
    }

    //constructor usado en crear usuario
    public Usuario(String rut, String nombres, String apellidos, String email, String estado, String genero, String contrasena, String cargo,Rol rol, LocalDate fecha_nac, String telefono) {
        this.rut = rut;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estado = estado;
        this.genero = genero;
        this.contrasena = contrasena;
        this.cargo = cargo;
        this.rol = rol;
        this.fecha_nac = fecha_nac;
        this.telefono = telefono;
        this.email = email;
    }

    public Usuario(int ID, String rut, String nombres, String apellidos, String email, String estado, String genero,String cargo, Rol rol, LocalDate fecha_nac, String telefono) {
        this.ID = ID;
        this.rut = rut;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.estado = estado;
        this.genero = genero;
        this.cargo = cargo;
        this.rol = rol;
        this.fecha_nac = fecha_nac;
        this.telefono = telefono;
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

    public void setRol(Rol rol) { this.rol = rol; }

    public void setFecha_nac(LocalDate fecha_nac) {
        this.fecha_nac = fecha_nac;
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

    public Rol getRol() { return rol; }

    public LocalDate getFecha_nac() {
        return fecha_nac;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getGenero() {
        return genero;
    }

}
