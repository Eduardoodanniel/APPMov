package com.example.geo.model;

import java.util.Date;

public class Coordenada {
    
    private Long id;
    private Usuario usuario;
    private double latitud;
    private double longitud;
    private Date fecha;

    public Coordenada() {
    }

    public Coordenada(Long id, Usuario usuario, double latitud, double longitud, Date fecha) {
        this.id = id;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

