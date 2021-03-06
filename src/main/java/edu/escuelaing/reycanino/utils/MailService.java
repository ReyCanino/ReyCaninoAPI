package edu.escuelaing.reycanino.utils;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import edu.escuelaing.reycanino.model.Cliente;
import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.model.Reserva;
import edu.escuelaing.reycanino.persitence.DataBaseConnection;

@Service("Mail")
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	DataBaseConnection dbConnection;

	static final String SENDER = "reycaninostore@gmail.com";
	static final String COD = "UTF-8";

	static final Logger LOG = Logger.getLogger("edu.escuelaing.reycanino.utils.MailService");

	public void sendValidationEmail(Horario horario) {
		MimeMessagePreparator preparator = null;

		try {

			final Cliente shop = dbConnection.buscarCliente(horario.getTiendaCanina());
			final Horario horarioDB = dbConnection.buscarHorario(horario.getId());
			final Cliente cliente = dbConnection.buscarCliente(horario.getReserva().getCliente());

			preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(cliente.getCorreo());
					message.setFrom(SENDER);
					message.setSubject("┬íConfirma tu reserva!");

					String url = "https://reycanino-api.herokuapp.com/reyCanino/confirmar/";
					url += horario.getReserva().getId();

					String dateAux = "";
					int dia = horarioDB.getFi().getDayOfMonth();
					int mes = horarioDB.getFi().getMonth().getValue();
					int anio = horarioDB.getFi().getYear();
					int hora = horarioDB.getFi().getHour();
					int minuto = horarioDB.getFi().getMinute();
					String min = (minuto > 9) ? Integer.toString(minuto) : "0" + minuto;

					dateAux = dia + "/" + mes + "/" + anio + " - " + hora + ":" + min;

					VelocityContext velocityContext = new VelocityContext();
					velocityContext.put("user", cliente.getNombre());
					velocityContext.put("nombreMascota", horario.getReserva().getNombreMascota());
					velocityContext.put("nombreTienda", shop.getNombre());
					velocityContext.put("direccion", shop.getDireccion());
					velocityContext.put("nombreServicio", horarioDB.getServicio());
					velocityContext.put("pagina", url);
					velocityContext.put("fecha", dateAux);

					StringWriter text = new StringWriter();
					VelocityEngine ve = new VelocityEngine();
					ve.mergeTemplate("validacion.vm", COD, velocityContext, text);
					message.setText(text.toString(), true);
				}
			};
		} catch (Exception e) {
			LOG.log(Level.INFO, e.getLocalizedMessage());
		}
		mailSender.send(preparator);
	}

	public void sendConfirmationEmail(Horario horario) {
		MimeMessagePreparator preparator = null;
		try {

			final Cliente shop = dbConnection.buscarCliente(horario.getTiendaCanina());
			final Cliente cliente = dbConnection.buscarCliente(horario.getReserva().getCliente());

			preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(cliente.getCorreo());
					message.setFrom(SENDER);
					message.setSubject("┬íResumen de tu reserva!");

					String dateAux = "";
					int dia = horario.getFi().getDayOfMonth();
					int mes = horario.getFi().getMonth().getValue();
					int anio = horario.getFi().getYear();
					int hora = horario.getFi().getHour();
					int minuto = horario.getFi().getMinute();
					String min = (minuto > 9) ? Integer.toString(minuto) : "0" + minuto;

					dateAux = dia + "/" + mes + "/" + anio + " - " + hora + ":" + min;

					VelocityContext velocityContext = new VelocityContext();
					velocityContext.put("user", horario.getReserva().getCliente());
					velocityContext.put("nombreMascota", horario.getReserva().getNombreMascota());
					velocityContext.put("codigo", horario.getId());
					velocityContext.put("nombreTienda", shop.getNombre());
					velocityContext.put("direccion", shop.getDireccion());
					velocityContext.put("nombreServicio", horario.getServicio());
					velocityContext.put("fecha", dateAux);

					StringWriter text = new StringWriter();
					VelocityEngine ve = new VelocityEngine();
					ve.mergeTemplate("plantilla.vm", COD, velocityContext, text);
					message.setText(text.toString(), true);
				}
			};
		} catch (Exception e) {
			LOG.log(Level.INFO, e.getLocalizedMessage());
		}
		this.mailSender.send(preparator);
	}

	public void sendErrorConfirmationEmail(Reserva reserva) {

		MimeMessagePreparator preparator = null;
		try {
			final Cliente cliente = dbConnection.buscarCliente(reserva.getCliente());
			preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(cliente.getCorreo());
					message.setFrom(SENDER);
					message.setSubject("┬íOps ha ocurrido un error!");

					VelocityContext velocityContext = new VelocityContext();
					velocityContext.put("user", reserva.getCliente());

					StringWriter text = new StringWriter();
					VelocityEngine ve = new VelocityEngine();
					ve.mergeTemplate("error.vm", COD, velocityContext, text);
					message.setText(text.toString(), true);
				}
			};
		} catch (Exception e) {
			LOG.log(Level.INFO, e.getLocalizedMessage());
		}
		this.mailSender.send(preparator);
	}

}