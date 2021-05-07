package com.example.geo.model;

import java.util.Date;

public class Telefono {

    private Long id;
    private Usuario usuario;
    private String noTelefono;
    private String imei;
    private double bateria;
    private String modelo;
    private String versionAndroid;
    private double almacenamientoTotal;
    private double almacenamientoDisponible;
    private double ram;
    private Date fecha;

    public Telefono() {
    }

    public Telefono(Long id, Usuario usuario, String noTelefono, String imei, double bateria, String modelo,
                    String versionAndroid, double almacenamientoTotal, double almacenamientoDisponible, double ram,
                    Date fecha) {
        this.id = id;
        this.usuario = usuario;
        this.noTelefono = noTelefono;
        this.imei = imei;
        this.bateria = bateria;
        this.modelo = modelo;
        this.versionAndroid = versionAndroid;
        this.almacenamientoTotal = almacenamientoTotal;
        this.almacenamientoDisponible = almacenamientoDisponible;
        this.ram = ram;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNoTelefono() {
        return noTelefono;
    }

    public void setNoTelefono(String noTelefono) {
        this.noTelefono = noTelefono;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public double getBateria() {
        return bateria;
    }

    public void setBateria(double bateria) {
        this.bateria = bateria;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getVersionAndroid() {
        return versionAndroid;
    }

    public void setVersionAndroid(String versionAndroid) {
        this.versionAndroid = versionAndroid;
    }

    public double getAlmacenamientoTotal() {
        return almacenamientoTotal;
    }

    public void setAlmacenamientoTotal(double almacenamientoTotal) {
        this.almacenamientoTotal = almacenamientoTotal;
    }

    public double getAlmacenamientoDisponible() {
        return almacenamientoDisponible;
    }

    public void setAlmacenamientoDisponible(double almacenamientoDisponible) {
        this.almacenamientoDisponible = almacenamientoDisponible;
    }

    public double getRam() {
        return ram;
    }

    public void setRam(double ram) {
        this.ram = ram;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
