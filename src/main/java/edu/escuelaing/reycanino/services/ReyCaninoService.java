package edu.escuelaing.reycanino.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.escuelaing.reycanino.model.Cliente;
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
        return sender.reservar(horario);
    }

    public String confirmar(String id) throws ReyCaninoException {
        Reserva reserva = dbService.buscarReserva(id);
        OffsetDateTime now = OffsetDateTime.now();
        if (reserva == null)
            throw new ReyCaninoException(ReyCaninoException.NO_EXISTE_RESERVA);
        if (now.isAfter(reserva.getFechaLimite()))
            throw new ReyCaninoException(ReyCaninoException.TIEMPO_EXPIRADO);
        return sender.confirmar(reserva);
    }

    public Horario consultarReserva(String id) {
        return dbService.buscarHorario(id);
    }

    public List<Horario> consultarHorario(String id) {
        return dbService.buscarHorarioCliente(id);
    }

    public List<Cliente> consultarTiendas() {
        return dbService.buscarTiendas();
    }

    public List<Horario> consultarHorarioAdmin(String id) {
        return dbService.buscarHorarioAdmin(id);
    }

    public Cliente consultarTiendaServicio (String service) {
        return dbService.buscarTiendaServicio(service);
    }

    public String cancelarReserva(String id) {
        Horario horario = dbService.buscarHorario(id);
        if (horario.getReserva() != null && horario.getFi().isAfter(OffsetDateTime.now())) {
            dbService.cancelarReserva(id);
            return "Éxito";
        }
        return "Fallo";
    }

    public void cancelarHorario(String id) {
        dbService.eliminarHorario(id);
        
    }

    public Cliente consultarCliente(String cliente) {
        return dbService.buscarCliente(cliente);
    }

    public Cliente login(Cliente cliente) {
        return dbService.login(cliente);
    }

    public Horario agregarHorario(Horario horario) {
        return dbService.agregarHorario(horario);
    }

    public void agregarCliente(Cliente cliente) {
        dbService.agregarCliente(cliente);
    }
}