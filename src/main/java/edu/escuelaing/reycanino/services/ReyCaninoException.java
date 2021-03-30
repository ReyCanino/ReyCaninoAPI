package edu.escuelaing.reycanino.services;

public class ReyCaninoException extends Exception {

    private static final long serialVersionUID = 1L;
    public static final String NO_EXISTE_RESERVA = "LA RESERVA NO EXISTE";
    public static final String TIEMPO_EXPIRADO = "EL TIEMPO DE LA RESERVA HA EXPIRADO";

    public ReyCaninoException(String message) {
        super(message);
    }

}
