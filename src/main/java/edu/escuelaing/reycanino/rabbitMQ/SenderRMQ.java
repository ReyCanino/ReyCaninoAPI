package edu.escuelaing.reycanino.rabbitMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.model.Reserva;

@Service("Sender")
public class SenderRMQ {

	public String reservar(Horario horario) {
		String m = "";
		if (horario != null) {
			m += "reservar|";
			m += horario.getId() + ",";
			m += horario.getReserva().getCliente() + ",";
			m += horario.getReserva().getNombreMascota() + ",";
			m += horario.getReserva().getComentario() + ",";
			m += horario.getTiendaCanina() + ",";
			m += horario.getReserva().getRazaMascota();
			sendMessage(m);
		}
		return m;
	}

	public String confirmar(Reserva reserva) {
		String m = "";
		if (reserva != null) {
			m += "confirmar|";
			m += reserva.getId() + ",";
			m += reserva.getHorario();
			sendMessage(m);
		}
		return m;
	}

	private void sendMessage(String message) {
		Channel channel = ConnectionRMQ.create();
		try {
			channel.basicPublish("", ConfigurationRMQ.QUEUE_NAME, null, message.getBytes());
			channel.close();
		} catch (IOException e) {
			System.err.println(e);
		} catch (TimeoutException e) {
			System.err.println(e);
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				System.err.println(e);
			} catch (TimeoutException e) {
				System.err.println(e);
			}
		}

	}
}