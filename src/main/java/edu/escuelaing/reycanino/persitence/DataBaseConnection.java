package edu.escuelaing.reycanino.persitence;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Util;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import edu.escuelaing.reycanino.db.RethinkDBConnectionFactory;
import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.model.Reserva;
import edu.escuelaing.reycanino.model.Cliente;

@Service("DbConnection")
public class DataBaseConnection {

    static RethinkDB r = RethinkDB.r;
    static Connection connection;

    public List<Horario> disponibilidad(Horario horarioConsulta) {
        String[] servicios = { "Peluqueria", "Paseo" };
        int a1, a2, m1, m2, d1, d2;
        Calendar c = Calendar.getInstance();

        c.setTime(horarioConsulta.getFechaConsulta());
        a1 = c.get(Calendar.YEAR);
        m1 = c.get(Calendar.MONTH) + 1;
        d1 = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_YEAR, 1);
        a2 = c.get(Calendar.YEAR);
        m2 = c.get(Calendar.MONTH) + 1;
        d2 = c.get(Calendar.DAY_OF_MONTH);

        connection = RethinkDBConnectionFactory.createConnection();

        ArrayList<Horario> query = r.db("ReyCanino").table("Horario")
                .filter(horario -> horario.getField("tiendaCanina").eq(horarioConsulta.getTiendaCanina()))
                .filter(horario -> horario.getField("servicio")
                        .eq(servicios[Integer.parseInt(horarioConsulta.getServicio()) - 1]))
                .filter(horario -> horario.g("fi").during(r.time(a1, m1, d1, "Z"), r.time(a2, m2, d2, "Z")))
                .filter(horario -> horario.g("reserva").eq(null)).orderBy("fi").run(connection, Horario.class);
        ArrayList<Horario> l = new ArrayList<Horario>();

        for (int i = 0; i < query.size(); i++) {
            l.add(Util.convertToPojo(query.get(i), Optional.of(Horario.class)));
        }

        return l;
    }

    public Cliente buscarCliente(String id) {

        connection = RethinkDBConnectionFactory.createConnection();
        Cursor<Cliente> query = r.db("ReyCanino").table("Cliente").filter(cliente -> cliente.getField("id").eq(id))
                .run(connection, Cliente.class);

        Cliente tiendaCanina = null;

        while (query.hasNext()) {
            tiendaCanina = query.next();
        }
        return tiendaCanina;
    }

    public Horario buscarHorario(String id) {
        Horario horario = null;
        connection = RethinkDBConnectionFactory.createConnection();
        Cursor<Horario> query = r.db("ReyCanino").table("Horario").filter(hora -> hora.getField("id").eq(id))
                .run(connection, Horario.class);
        while (query.hasNext()) {
            horario = query.next();
        }
        return horario;
    }

    public Reserva buscarReserva(String id) {
        Reserva reserva = null;
        connection = RethinkDBConnectionFactory.createConnection();
        Cursor<Reserva> query = r.db("ReyCanino").table("Reserva").filter(res -> res.getField("id").eq(id))
                .run(connection, Reserva.class);
        while (query.hasNext()) {
            reserva = query.next();
        }
        return reserva;
    }

    public void actualizarHorario(Horario horario) {
        connection = RethinkDBConnectionFactory.createConnection();
        r.db("ReyCanino").table("Horario").get(horario.getId()).update(r.hashMap("reserva",
                // update null horario
                r.hashMap("cliente", horario.getReserva().getCliente())
                        .with("nombreMascota", horario.getReserva().getNombreMascota())
                        .with("comentario", horario.getReserva().getComentario())
                        .with("razaMascota", horario.getReserva().getRazaMascota())))
                .run(connection);
    }

    public Horario insertarReserva(Horario horario) {
        OffsetDateTime nowDateTime = OffsetDateTime.now();
        nowDateTime.plusDays(1);
        connection = RethinkDBConnectionFactory.createConnection();

        HashMap<String, Object> insert = r.db("ReyCanino").table("Reserva")
                .insert(r.array(r.hashMap("fechaLimite", nowDateTime).with("cliente", horario.getReserva().getCliente())
                        .with("nombreMascota", horario.getReserva().getNombreMascota())
                        .with("comentario", horario.getReserva().getComentario())
                        .with("raza", horario.getReserva().getRazaMascota()).with("horario", horario.getId())))
                .run(connection);
        // ArrayList<String> llaves = (ArrayList<String>) insert.get("generated_keys");
        // horario.getReserva().setId(llaves.get(0));
        return horario;
    }

    public Horario consultarReserva(String id) {
        connection = RethinkDBConnectionFactory.createConnection();
        Cursor<Horario> c = r.db("ReyCanino").table("Horario").filter(res -> res.getField("id").eq(id)).run(connection,
                Horario.class);
        Horario horario = null;
        for (Horario o : c) {
            horario = o;
        }
        return horario;
    }

    public void eliminarReserva(Reserva reserva) {
        connection = RethinkDBConnectionFactory.createConnection();
        r.db("ReyCanino").table("Reserva").get(reserva.getId()).delete().run(connection);
    }

    public void cancelarReserva(String id) {
        connection = RethinkDBConnectionFactory.createConnection();
        r.db("ReyCanino").table("HORARIO").filter(res -> res.getField("id").eq(id)).update(r.hashMap("reserva", null))
                .run(connection);
    }
}