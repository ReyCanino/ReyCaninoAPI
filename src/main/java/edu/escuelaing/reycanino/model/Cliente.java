package edu.escuelaing.reycanino.model;

public class Cliente {

    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;
    private String id;
    private String tipo;
    private String psw;

    public Cliente() {
        // Do nothing
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPsw() {
        return this.psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    @Override
    public String toString() {
        return "{" + " nombre='" + getNombre() + "'" + ", telefono='" + getTelefono() + "'" + ", correo='" + getCorreo()
                + "'" + ", direccion='" + getDireccion() + "'" + ", id='" + getId() + "'" + ", tipo='" + getTipo() + "'"
                + ", psw='" + getPsw() + "'" + "}";
    }

}