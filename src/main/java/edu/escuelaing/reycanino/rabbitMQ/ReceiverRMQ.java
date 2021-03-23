package edu.escuelaing.reycanino.rabbitMQ;

import com.rabbitmq.client.*;

import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.model.Reserva;
import edu.escuelaing.reycanino.persitence.DataBaseConnection;
import edu.escuelaing.reycanino.utils.MailService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("Receiver")
public class ReceiverRMQ {

	@Autowired
	MailService mailService;

	@Autowired
	DataBaseConnection dbConnection;

	public void recibir(String message) {

		String[] values = message.split("\\|");
		String[] data = values[1].split(",");
		if (values[0].equals("reservar")) {

			Horario horario = new Horario();
			Reserva reserva = new Reserva();

			horario.setId(data[0]);
			horario.setTiendaCanina(data[4]);
			reserva.setCliente(data[1]);
			reserva.setNombreMascota(data[2]);
			reserva.setComentario(data[3]);
			reserva.setRazaMascota(data[5]);
			horario.setReserva(reserva);
			horario = dbConnection.insertarReserva(horario);

			mailService.sendValidationEmail(horario);

		} else if (values[0].equals("confirmar")) {

			Reserva reserva = dbConnection.buscarReserva(data[0]);
			Horario horario = dbConnection.buscarHorario(reserva.getHorario());

			if (horario.getReserva() == null) {
				horario.setReserva(reserva);

				dbConnection.actualizarHorario(horario);
				mailService.sendConfirmationEmail(horario);
			} else
				mailService.sendErrorConfirmationEmail(reserva);
			dbConnection.eliminarReserva(reserva);
		}
	}

	public void receiveMesssage() {
		Channel channel = ConnectionRMQ.create();
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				recibir(message);
			}
		};
		try {
			channel.basicConsume(ConfigurationRMQ.QUEUE_NAME, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}