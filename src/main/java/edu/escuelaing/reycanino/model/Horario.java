package edu.escuelaing.reycanino.model;

import java.time.OffsetDateTime;
import java.util.Date;

public class Horario {
    private String servicio;
    private String id;
    private String tiendaCanina;
    private OffsetDateTime ff;
    private OffsetDateTime fi;
    private Date fechaConsulta;
    private Reserva reserva;
    private String tipoRepeticion;
    private Integer cantRepeticiones;

    public Horario(String servicio, String id, String tiendaCanina, OffsetDateTime ff, OffsetDateTime fi,
            Date fechaConsulta, Reserva reserva, String tipoRepeticion, Integer cantRepeticiones) {
        this.servicio = servicio;
        this.id = id;
        this.tiendaCanina = tiendaCanina;
        this.ff = ff;
        this.fi = fi;
        this.fechaConsulta = fechaConsulta;
        this.reserva = reserva;
        this.tipoRepeticion = tipoRepeticion;
        this.cantRepeticiones = cantRepeticiones;
    }

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTiendaCanina() {
        return tiendaCanina;
    }

    public void setTiendaCanina(String tiendaCanina) {
        this.tiendaCanina = tiendaCanina;
    }

    public Horario() {
    }

    public OffsetDateTime getFf() {
        return ff;
    }

    public void setFf(OffsetDateTime ff) {
        this.ff = ff;
    }

    public OffsetDateTime getFi() {
        return fi;
    }

    public void setFi(OffsetDateTime fi) {
        this.fi = fi;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public String getTipoRepeticion() {
        return this.tipoRepeticion;
    }

    public void setTipoRepeticion(String tipoRepeticion) {
        this.tipoRepeticion = tipoRepeticion;
    }

    public Integer getCantRepeticiones() {
        return this.cantRepeticiones;
    }

    public void setCantRepeticiones(Integer cantRepeticiones) {
        this.cantRepeticiones = cantRepeticiones;
    }

    @Override
    public String toString() {
        return "{" + " servicio='" + getServicio() + "'" + ", id='" + getId() + "'" + ", tiendaCanina='"
                + getTiendaCanina() + "'" + ", ff='" + getFf() + "'" + ", fi='" + getFi() + "'" + ", fechaConsulta='"
                + getFechaConsulta() + "'" + ", reserva='" + getReserva() + "'" + ", tipoRepeticion='"
                + getTipoRepeticion() + "'" + ", cantRepeticiones='" + getCantRepeticiones() + "'" + "}";
    }

}