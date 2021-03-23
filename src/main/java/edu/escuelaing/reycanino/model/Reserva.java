package edu.escuelaing.reycanino.model;

import java.time.OffsetDateTime;

public class Reserva {
    private String id;
    private String cliente;
    private String comentario;
    private String nombreMascota;
    private String razaMascota;
    private String horario;
    private OffsetDateTime fechaLimite;

    public Reserva() {
    }

    public Reserva(String cliente, String comentario, String nombreMascota, String razaMascota,
            OffsetDateTime fechaLimite) {
        this.cliente = cliente;
        this.comentario = comentario;
        this.nombreMascota = nombreMascota;
        this.razaMascota = razaMascota;
        this.fechaLimite = fechaLimite;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public OffsetDateTime getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(OffsetDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNombreMascota() {
        return this.nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getRazaMascota() {
        return this.razaMascota;
    }

    public void setRazaMascota(String razaMascota) {
        this.razaMascota = razaMascota;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHorario() {
        return this.horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", cliente='" + getCliente() + "'" + ", comentario='" + getComentario()
                + "'" + ", nombreMascota='" + getNombreMascota() + "'" + ", razaMascota='" + getRazaMascota() + "'"
                + ", horario='" + getHorario() + "'" + ", fechaLimite='" + getFechaLimite() + "'" + "}";
    }

}