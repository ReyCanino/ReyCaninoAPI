package edu.escuelaing.reycanino;

import org.junit.Test;

import edu.escuelaing.reycanino.services.ReyCaninoException;

public class ExceptionTest {

    @Test
    public void exceptionTest() {
        Exception e = new ReyCaninoException("Mensaje de prueba");
        System.out.println(e.getLocalizedMessage());
    }
}
