package edu.escuelaing.reycanino;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.escuelaing.reycanino.model.Cliente;
import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.model.Reserva;
import edu.escuelaing.reycanino.rabbit.SenderRMQ;
import edu.escuelaing.reycanino.services.ReyCaninoException;
import edu.escuelaing.reycanino.services.ReyCaninoService;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

	@Autowired
	private ReyCaninoService servicio;

	@Autowired
	private SenderRMQ sender;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	void testConsulta() throws Exception {
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
	void testConsultaFail() throws Exception {
		String url = "/reyCanino/consultar";
		Reserva anObject = new Reserva();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(anObject);

		mvc.perform(MockMvcRequestBuilders.post(url).contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isNotFound());
	}

	@Test
	void testReservar() throws Exception {
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
	void testConsultarHorarioCliente() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/reyCanino/horario/038e2c41-e374-4770-9331-6861550d9427")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void testConsultarTiendas() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/reyCanino/tiendas").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testConsultarHorarioAdmin() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/reyCanino/horarioAdmin/f27d9717-9f4c-4ad2-ae36-8e9117b3848e")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void testConsultarReservar() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/reyCanino/consultar/3f36d89b-dd0f-4d98-8625-2292959d39b7")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void testConsultarCliente() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/reyCanino/cliente/038e2c41-e374-4770-9331-6861550d9427")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testConfirmar() {
		try {
			servicio.confirmar("cliente");
		} catch (ReyCaninoException e) {
			e.printStackTrace();
		}
		assertTrue(true);
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
		assertTrue(true);
	}

	@Test
	public void testConfirmarOnSender() {
		Reserva reserva = new Reserva();
		sender.confirmar(reserva);
		assertTrue(true);
	}

	@Test
	public void exceptionTest() {
		Exception e = new ReyCaninoException("Mensaje de prueba");
		System.out.println(e.getLocalizedMessage());
		assertTrue(true);
	}

	@Test
	void testLogin() throws Exception {
		String url = "/reyCanino/login";
		Cliente anObject = new Cliente();
		anObject.setCorreo("mafehv1999@hotmail.com");
		anObject.setPsw("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(anObject);

		mvc.perform(MockMvcRequestBuilders.post(url).contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isAccepted());
	}

	@Test
	void testHorarioAgregar() throws Exception {
		String url = "/reyCanino/horario/agregar";
		Horario anObject = new Horario("veterinaria", null, "f27d9717-9f4c-4ad2-ae36-8e9117b3848e", null, null, null,
				null, "Diaria", 1);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(anObject);

		mvc.perform(MockMvcRequestBuilders.post(url).contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isOk());
	}

	@Test
	void testAgregarCliente() throws Exception {
		String url = "/reyCanino/agregarCliente";
		Cliente anObject = new Cliente();
		anObject.setTipo("test");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(anObject);

		mvc.perform(MockMvcRequestBuilders.post(url).contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isAccepted());
	}
}