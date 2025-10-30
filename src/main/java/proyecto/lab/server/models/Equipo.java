package proyecto.lab.server.models;

import proyecto.lab.server.dto.EquipoDTO;

import java.time.LocalDate;

public class Equipo {
    private int id_equipo;
    private int id_admin;
    private int id_lab_equipo;
    private String hostname;
    private String numero_serie;
    private String fabricante;
    private String estado;
    private String modelo;
    private String mac;
    private String ip;
    private String modeloCPU;
    private String nucleosCPU;
    private String ramTotal;
    private String almacenamiento;
    private String modeloGPU;
    private LocalDate fecha_ingreso;
    private String nombreLab;


    //constructor vacío
    public Equipo() {}


    // Constructor
    public Equipo(int id_equipo, int id_admin, int id_lab_equipo, String hostname, String numero_serie,
                  String fabricante, String estado, String modelo, String mac, String ip, String modeloCPU,
                  String nucleosCPU, String ramTotal, String almacenamiento, String modeloGPU, LocalDate fecha_ingreso) {
        this.id_equipo = id_equipo;
        this.id_admin = id_admin;
        this.id_lab_equipo = id_lab_equipo;
        this.hostname = hostname;
        this.numero_serie = numero_serie;
        this.fabricante = fabricante;
        this.estado = estado;
        this.modelo = modelo;
        this.mac = mac;
        this.ip = ip;
        this.modeloCPU = modeloCPU;
        this.nucleosCPU = nucleosCPU;
        this.ramTotal = ramTotal;
        this.almacenamiento = almacenamiento;
        this.modeloGPU = modeloGPU;
        this.fecha_ingreso = fecha_ingreso;
    }

    public Equipo(int id_equipo, int id_admin, int id_lab_equipo, String hostname, String numero_serie,
                  String fabricante, String estado, String modelo, String mac, String ip, String modeloCPU,
                  String nucleosCPU, String ramTotal, String almacenamiento, String modeloGPU, LocalDate fecha_ingreso, String nombreLab) {
        this.id_equipo = id_equipo;
        this.id_admin = id_admin;
        this.id_lab_equipo = id_lab_equipo;
        this.hostname = hostname;
        this.numero_serie = numero_serie;
        this.fabricante = fabricante;
        this.estado = estado;
        this.modelo = modelo;
        this.mac = mac;
        this.ip = ip;
        this.modeloCPU = modeloCPU;
        this.nucleosCPU = nucleosCPU;
        this.ramTotal = ramTotal;
        this.almacenamiento = almacenamiento;
        this.modeloGPU = modeloGPU;
        this.fecha_ingreso = fecha_ingreso;
        this.nombreLab = nombreLab;
    }

    //Constructor que recibe un DTO
    public Equipo(EquipoDTO dto) {
        this.id_equipo = dto.id_equipo();
        this.id_admin = dto.id_admin();
        this.id_lab_equipo = dto.id_lab_equipo();
        this.hostname = dto.hostname();
        this.numero_serie = dto.numero_serie();
        this.fabricante = dto.fabricante();
        this.estado = dto.estado();
        this.modelo = dto.modelo();
        this.mac = dto.mac();
        this.ip = dto.ip();
        this.modeloCPU = dto.modeloCPU();
        this.nucleosCPU = dto.nucleosCPU();
        this.ramTotal = dto.ramTotal();
        this.almacenamiento = dto.almacenamiento();
        this.modeloGPU = dto.modeloGPU();
        this.fecha_ingreso = dto.fecha_ingreso();
    }

    //Setters
    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public void setId_lab_equipo(int id_lab_equipo) {
        this.id_lab_equipo = id_lab_equipo;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setNumero_serie(String numero_serie) {
        this.numero_serie = numero_serie;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setModeloCPU(String modeloCPU) {
        this.modeloCPU = modeloCPU;
    }

    public void setNucleosCPU(String nucleosCPU) {
        this.nucleosCPU = nucleosCPU;
    }

    public void setRamTotal(String ramTotal) {
        this.ramTotal = ramTotal;
    }

    public void setAlmacenamiento(String almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public void setModeloGPU(String modeloGPU) {
        this.modeloGPU = modeloGPU;
    }

    public void setFecha_ingreso(LocalDate fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }


    //Getters
    public int getId_equipo() {
        return id_equipo;
    }

    public int getId_admin() {
        return id_admin;
    }

    public int getId_lab_equipo() {
        return id_lab_equipo;
    }

    public String getHostname() {
        return hostname;
    }

    public String getNumero_serie() {
        return numero_serie;
    }

    public String getFabricante() {
        return fabricante;
    }

    public String getEstado() {
        return estado;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMac() {
        return mac;
    }

    public String getIp() {
        return ip;
    }

    public String getModeloCPU() {
        return modeloCPU;
    }

    public String getNucleosCPU() {
        return nucleosCPU;
    }

    public String getRamTotal() {
        return ramTotal;
    }

    public String getAlmacenamiento() {
        return almacenamiento;
    }

    public String getModeloGPU() {
        return modeloGPU;
    }

    public LocalDate getFecha_ingreso() {
        return fecha_ingreso;
    }
}

