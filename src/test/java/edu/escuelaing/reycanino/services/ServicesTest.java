package edu.escuelaing.reycanino.services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.model.Reserva;
import edu.escuelaing.reycanino.rabbit.SenderRMQ;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicesTest {
    @Autowired
    private ReyCaninoService servicio;

    @Autowired
    private SenderRMQ sender;

    @Test
    public void testConfirmar() {
        try {
            servicio.confirmar("cliente");
        } catch (ReyCaninoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReservarOnSender() {
        Horario anObject = new Horario();
        Reserva reserva = new Reserva();
        anObject.setFechaConsulta(new Date());
        anObject.setServicio("1");
        anObject.setTiendaCanina("1234");
        anObject.setReserva(reserva);
        sender.reservar(anObject);
    }

    @Test
    public void testConfirmarOnSender() {
        Reserva reserva = new Reserva();
        sender.confirmar(reserva);
    }
}