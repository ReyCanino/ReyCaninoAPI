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
    private static final String HOST = "ec2-54-160-111-65.compute-1.amazonaws.com";
    private static final String DB_NAME = "ReyCanino";
    private static final String TABLE_HORARIO = "Horario";
    private static final String TABLE_RESERVA = "Reserva";
    private static final String TABLE_CLIENTE = "Cliente";
    private static final String HORARIO_LABEL = "horario";
    private static final String RESERVA_LABEL = "reserva";
    private static final String CLIENTE_LABEL = "cliente";
    private static final String Tienda_Canina = "tiendaCanina";
    private static final int PORT = 32769;

    private void createConnection() {
        connection = RethinkDB.r.connection().hostname(HOST).port(PORT).connect();
    }

    public List<Horario> disponibilidad(Horario horarioConsulta) {
        String[] servicios = { "peluqueria", "veterinaria" };
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
        createConnection();

        ArrayList<Horario> query = r.db(DB_NAME).table(TABLE_HORARIO)
                .filter(horario -> horario.getField(Tienda_Canina).eq(horarioConsulta.getTiendaCanina()))
                .filter(horario -> horario.getField("servicio")
                        .eq(servicios[Integer.parseInt(horarioConsulta.getServicio()) - 1]))
                .filter(horario -> horario.g("fi").during(r.time(a1, m1, d1, "Z"), r.time(a2, m2, d2, "Z")))
                .filter(horario -> horario.g(RESERVA_LABEL).eq(null)).orderBy("fi").run(connection, Horario.class);
        ArrayList<Horario> l = new ArrayList<>();
        for (int i = 0; i < query.size(); i++) {
            l.add(Util.convertToPojo(query.get(i), Optional.of(Horario.class)));
        }
        connection.close();

        return l;
    }

    public Cliente buscarCliente(String id) {

        createConnection();
        Cursor<Cliente> query = r.db(DB_NAME).table(TABLE_CLIENTE).filter(cliente -> cliente.getField("id").eq(id))
                .run(connection, Cliente.class);

        Cliente tiendaCanina = null;

        while (query.hasNext()) {
            tiendaCanina = query.next();
        }
        connection.close();
        return tiendaCanina;
    }

    public Horario buscarHorario(String id) {
        Horario horario = null;
        createConnection();
        Cursor<Horario> query = r.db(DB_NAME).table(TABLE_HORARIO).filter(hora -> hora.getField("id").eq(id))
                .run(connection, Horario.class);
        while (query.hasNext()) {
            horario = query.next();
        }
        connection.close();
        return horario;
    }

    public List<Cliente> buscarTiendas() {
        List<Cliente> clientes = new ArrayList<>();
        createConnection();

        Cursor<Cliente> query = r.db(DB_NAME).table(TABLE_CLIENTE)
                .filter(cliente -> cliente.getField("tipo").eq("admin")).run(connection, Cliente.class);
        while (query.hasNext()) {
            clientes.add(query.next());
        }
        connection.close();
        return clientes;
    }

    public List<Horario> buscarHorarioCliente(String id) {
        List<Horario> horario = new ArrayList<>();
        createConnection();
        Cursor<Horario> query = r.db(DB_NAME).table(TABLE_HORARIO)
                .filter(res -> res.getField(RESERVA_LABEL).getField(CLIENTE_LABEL).eq(id))
                .run(connection, Horario.class);
        while (query.hasNext()) {
            horario.add(query.next());
        }
        connection.close();
        return horario;
    }

    public List<Horario> buscarHorarioAdmin(String id) {
        createConnection();
        ArrayList<Horario> query = r.db(DB_NAME).table(TABLE_HORARIO).filter(res -> res.getField(Tienda_Canina).eq(id).orderBy("fi"))
                .run(connection, Horario.class);

        ArrayList<Horario> l = new ArrayList<>();
        for (int i = 0; i < query.size(); i++) {
            l.add(Util.convertToPojo(query.get(i), Optional.of(Horario.class)));
        }

        connection.close();
        return l;
    }

    public Reserva buscarReserva(String id) {
        Reserva reserva = null;
        createConnection();
        Cursor<Reserva> query = r.db(DB_NAME).table(TABLE_RESERVA).filter(res -> res.getField("id").eq(id))
                .run(connection, Reserva.class);
        while (query.hasNext()) {
            reserva = query.next();
        }
        connection.close();
        return reserva;
    }

    public void actualizarHorario(Horario horario) {
        createConnection();
        r.db(DB_NAME).table(TABLE_HORARIO).get(horario.getId()).update(r.hashMap(RESERVA_LABEL,
                // update null horario
                r.hashMap("cliente", horario.getReserva().getCliente())
                        .with("nombreMascota", horario.getReserva().getNombreMascota())
                        .with("comentario", horario.getReserva().getComentario())
                        .with("razaMascota", horario.getReserva().getRazaMascota())))
                .run(connection);
        connection.close();
    }

    public Horario insertarReserva(Horario horario) {
        OffsetDateTime nowDateTime = OffsetDateTime.now();
        nowDateTime = nowDateTime.plusDays(1);
        createConnection();

        HashMap<String, Object> insert = r.db(DB_NAME).table(TABLE_RESERVA)
                .insert(r.array(r.hashMap("fechaLimite", nowDateTime).with("cliente", horario.getReserva().getCliente())
                        .with("nombreMascota", horario.getReserva().getNombreMascota())
                        .with("comentario", horario.getReserva().getComentario())
                        .with("razaMascota", horario.getReserva().getRazaMascota())
                        .with(HORARIO_LABEL, horario.getId())))
                .run(connection);
        ArrayList<String> llaves = (ArrayList<String>) insert.get("generated_keys");
        horario.getReserva().setId(llaves.get(0));
        connection.close();
        return horario;
    }

    public void eliminarReserva(Reserva reserva) {
        createConnection();
        r.db(DB_NAME).table(TABLE_RESERVA).get(reserva.getId()).delete().run(connection);
        connection.close();
    }

    public void cancelarReserva(String id) {
        createConnection();
        r.db(DB_NAME).table(TABLE_HORARIO).filter(res -> res.getField("id").eq(id))
                .update(r.hashMap(RESERVA_LABEL, null)).run(connection);
        connection.close();
    }

    public Cliente login(Cliente cliente) {
        createConnection();
        Cursor<Cliente> query = r.db(DB_NAME).table(TABLE_CLIENTE)
                .filter(cl -> cl.getField("correo").eq(cliente.getCorreo()))
                .filter(cl -> cl.getField("contrasena").eq(cliente.getPsw())).run(connection, Cliente.class);
        Cliente clienteLogin = null;

        while (query.hasNext()) {
            clienteLogin = query.next();
        }
        connection.close();
        return clienteLogin;
    }

    public Horario agregarHorario(Horario horario) {
        createConnection();
        r.db(DB_NAME).table(TABLE_HORARIO)
                .insert(r.array(r.hashMap("ff", horario.getFf()).with("fi", horario.getFi())
                        .with("reserva", horario.getReserva()).with("servicio", horario.getServicio())
                        .with(Tienda_Canina, horario.getTiendaCanina())))
                .run(connection);

        connection.close();
        return horario;
    }

    public void agregarCliente(Cliente cliente) {
        createConnection();
        r.db(DB_NAME).table(TABLE_CLIENTE)
                .insert(r.array(r.hashMap("contrasena", cliente.getPsw()).with("correo", cliente.getCorreo())
                        .with("direccion", cliente.getDireccion()).with("nombre", cliente.getNombre())
                        .with("telefono", cliente.getTelefono()).with("tipo", cliente.getTipo())))
                .run(connection);
    }
}
