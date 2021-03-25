package edu.escuelaing.reycanino.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.model.Reserva;
import edu.escuelaing.reycanino.persitence.DataBaseConnection;
import edu.escuelaing.reycanino.rabbit.SenderRMQ;

@Service("ReyCanino")
public class ReyCaninoService {

    @Autowired
    DataBaseConnection dbService;

    @Autowired
    SenderRMQ sender;

    public List<Horario> consultsAvailableDates(Horario horario) {
        return dbService.disponibilidad(horario);
    }

    public String reservar(Horario horario) {
        String reservaMessage = sender.reservar(horario);
        return reservaMessage;
    }

    public String confirmar(String id) throws ReyCaninoException {
        Reserva reserva = dbService.buscarReserva(id);
        OffsetDateTime now = OffsetDateTime.now();
        if (reserva == null)
            throw new ReyCaninoException(ReyCaninoException.NO_EXISTE_RESERVA);
        if (now.isBefore(reserva.getFechaLimite()))
            throw new ReyCaninoException(ReyCaninoException.TIEMPO_EXPIRADO);
        String reservaMessage = sender.confirmar(reserva);
        return reservaMessage;
    }

    public Horario consultarReserva(String id) {
        Horario horario = dbService.consultarReserva(id);
        return horario;
    }

    public String cancelarReserva(String id) {
        Horario horario = dbService.consultarReserva(id);
        if (horario != null) {
            if (horario.getReserva() != null && horario.getFi().isAfter(OffsetDateTime.now())) {
                dbService.cancelarReserva(id);
                return "Éxito";
            }
        }
        return "Fallo";
    }

}