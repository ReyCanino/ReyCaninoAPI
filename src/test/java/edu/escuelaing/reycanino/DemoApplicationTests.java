package edu.escuelaing.reycanino;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.model.Reserva;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	public void testConsulta() throws Exception {
		String url = "/reyCanino/consultar";
		Horario anObject = new Horario();
		anObject.setFechaConsulta(new Date());
		anObject.setServicio("1");
		anObject.setTiendaCanina("1234");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(anObject);

		mvc.perform(MockMvcRequestBuilders.post(url).contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isAccepted());
	}

	@Test
	public void testReservar() throws Exception {
		String url = "/reyCanino/reservar";
		Horario anObject = new Horario();
		Reserva reserva = new Reserva();
		anObject.setFechaConsulta(new Date());
		anObject.setServicio("1");
		anObject.setTiendaCanina("1234");
		anObject.setReserva(reserva);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(anObject);

		mvc.perform(MockMvcRequestBuilders.post(url).contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isOk());
	}

	@Test
	public void testConfirmarReservar() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/reyCanino/confirmar/1234").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testConsultarReservar() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/reyCanino/consultar/1234").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}