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
import edu.escuelaing.reycanino.model.Horario;
import edu.escuelaing.reycanino.model.Reserva;
import edu.escuelaing.reycanino.model.Cliente;

@Service("DbConnection")
public class DataBaseConnection {

    private RethinkDB r = RethinkDB.r;
    private Connection connection;
    private final String host = "ec2-34-235-155-214.compute-1.amazonaws.com";
    private final String dbName = "ReyCanino";
    private final String tableHorarios = "Horario";
    private final String reservaLabel = "reserva";
    private final int port = 32769;

    private Connection createConnection() {
        return RethinkDB.r.connection().hostname(host).port(port).connect();
    }

    public List<Horario> disponibilidad(Horario horarioConsulta) {
        String[] servicios = { "Peluqueria", "Paseo" };
        int a1;
        int a2;
        int m1;
        int m2;
        int d1;
        int d2;
        Calendar c = Calendar.getInstance();

        c.setTime(horarioConsulta.getFechaConsulta());
        a1 = c.get(Calendar.YEAR);
        m1 = c.get(Calendar.MONTH) + 1;
        d1 = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_YEAR, 1);
        a2 = c.get(Calendar.YEAR);
        m2 = c.get(Calendar.MONTH) + 1;
        d2 = c.get(Calendar.DAY_OF_MONTH);

        connection = createConnection();

        ArrayList<Horario> query = r.db(dbName).table(tableHorarios)
                .filter(horario -> horario.getField("tiendaCanina").eq(horarioConsulta.getTiendaCanina()))
                .filter(horario -> horario.getField("servicio")
                        .eq(servicios[Integer.parseInt(horarioConsulta.getServicio()) - 1]))
                .filter(horario -> horario.g("fi").during(r.time(a1, m1, d1, "Z"), r.time(a2, m2, d2, "Z")))
                .filter(horario -> horario.g(reservaLabel).eq(null)).orderBy("fi").run(connection, Horario.class);
        ArrayList<Horario> l = new ArrayList<>();

        for (int i = 0; i < query.size(); i++) {
            l.add(Util.convertToPojo(query.get(i), Optional.of(Horario.class)));
        }

        return l;
    }

    public Cliente buscarCliente(String id) {

        connection = createConnection();
        Cursor<Cliente> query = r.db(dbName).table("Cliente").filter(cliente -> cliente.getField("id").eq(id))
                .run(connection, Cliente.class);

        Cliente tiendaCanina = null;

        while (query.hasNext()) {
            tiendaCanina = query.next();
        }
        return tiendaCanina;
    }

    public Horario buscarHorario(String id) {
        Horario horario = null;
        connection = createConnection();
        Cursor<Horario> query = r.db(dbName).table(tableHorarios).filter(hora -> hora.getField("id").eq(id))
                .run(connection, Horario.class);
        while (query.hasNext()) {
            horario = query.next();
        }
        return horario;
    }

    public Reserva buscarReserva(String id) {
        Reserva reserva = null;
        connection = createConnection();
        Cursor<Reserva> query = r.db(dbName).table(reservaLabel).filter(res -> res.getField("id").eq(id))
                .run(connection, Reserva.class);
        while (query.hasNext()) {
            reserva = query.next();
        }
        return reserva;
    }

    public void actualizarHorario(Horario horario) {
        connection = createConnection();
        r.db(dbName).table(tableHorarios).get(horario.getId()).update(r.hashMap(reservaLabel,
                // update null horario
                r.hashMap("cliente", horario.getReserva().getCliente())
                        .with("nombreMascota", horario.getReserva().getNombreMascota())
                        .with("comentario", horario.getReserva().getComentario())
                        .with("razaMascota", horario.getReserva().getRazaMascota())))
                .run(connection);
    }

    public Horario insertarReserva(Horario horario) {
        OffsetDateTime nowDateTime = OffsetDateTime.now();
        nowDateTime = nowDateTime.plusDays(1);
        connection = createConnection();

        HashMap<String, Object> insert = r.db(dbName).table(reservaLabel)
                .insert(r.array(r.hashMap("fechaLimite", nowDateTime).with("cliente", horario.getReserva().getCliente())
                        .with("nombreMascota", horario.getReserva().getNombreMascota())
                        .with("comentario", horario.getReserva().getComentario())
                        .with("raza", horario.getReserva().getRazaMascota()).with(tableHorarios, horario.getId())))
                .run(connection);
        ArrayList<String> llaves = (ArrayList<String>) insert.get("generated_keys");
        horario.getReserva().setId(llaves.get(0));
        return horario;
    }

    public Horario consultarReserva(String id) {
        connection = createConnection();
        Cursor<Horario> c = r.db(dbName).table(tableHorarios).filter(res -> res.getField("id").eq(id)).run(connection,
                Horario.class);
        Horario horario = null;
        for (Horario o : c) {
            horario = o;
        }
        return horario;
    }

    public void eliminarReserva(Reserva reserva) {
        connection = createConnection();
        r.db(dbName).table(reservaLabel).get(reserva.getId()).delete().run(connection);
    }

    public void cancelarReserva(String id) {
        connection = createConnection();
        r.db(dbName).table(tableHorarios).filter(res -> res.getField("id").eq(id)).update(r.hashMap(reservaLabel, null))
                .run(connection);
    }
}