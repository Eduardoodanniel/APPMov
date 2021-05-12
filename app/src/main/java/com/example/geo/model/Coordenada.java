package com.example.geo.model;

import java.util.Date;

public class Coordenada {

    private Long id;
    private String imei;
    private String bateria;
    private String almacenamientoDisponible;
    private double latitud;
    private double longitud;
    private Date fecha;

    public Coordenada() {
    }

    public Coordenada(Long id, String imei, String bateria, String almacenamientoDisponible, double latitud,
                      double longitud, Date fecha) {
        this.id = id;
        this.imei = imei;
        this.bateria = bateria;
        this.almacenamientoDisponible = almacenamientoDisponible;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBateria() {
        return bateria;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }

    public String getAlmacenamientoDisponible() {
        return almacenamientoDisponible;
    }

    public void setAlmacenamientoDisponible(String almacenamientoDisponible) {
        this.almacenamientoDisponible = almacenamientoDisponible;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
