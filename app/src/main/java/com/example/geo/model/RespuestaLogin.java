package com.example.geo.model;

public class RespuestaLogin {
    private String exception;
    private String mensajeAplication;
    private Long idUsuario;

    public RespuestaLogin(){

    }

    public RespuestaLogin(String exception, String mensajeAplication, Long idUsuario) {
        this.exception = exception;
        this.mensajeAplication = mensajeAplication;
        this.idUsuario = idUsuario;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMensajeAplication() {
        return mensajeAplication;
    }

    public void setMensajeAplication(String mensajeAplication) {
        this.mensajeAplication = mensajeAplication;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
