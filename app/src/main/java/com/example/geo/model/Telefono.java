package com.example.geo.model;

import java.util.Date;

public class Telefono {

    private Long id;
    private Long idUsuario;
    private String imei;
    private String noTelefono;
    private String modelo;
    private String versionAndroid;
    private String almacenamientoTotal;
    private String ram;
    private Date fecha;

    public Telefono() {
    }

    public Telefono(Long id, Long idUsuario, String imei, String noTelefono, String modelo, String versionAndroid,
                    String almacenamientoTotal, String ram, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.imei = imei;
        this.noTelefono = noTelefono;
        this.modelo = modelo;
        this.versionAndroid = versionAndroid;
        this.almacenamientoTotal = almacenamientoTotal;
        this.ram = ram;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getNoTelefono() {
        return noTelefono;
    }

    public void setNoTelefono(String noTelefono) {
        this.noTelefono = noTelefono;
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

    public String getAlmacenamientoTotal() {
        return almacenamientoTotal;
    }

    public void setAlmacenamientoTotal(String almacenamientoTotal) {
        this.almacenamientoTotal = almacenamientoTotal;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
