package edu.escuelaing.reycanino.persitence;

import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import com.rethinkdb.net.Util;

import edu.escuelaing.reycanino.model.Cliente;
import edu.escuelaing.reycanino.model.Horario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import com.rethinkdb.RethinkDB;

public class Main {
    public static void main(String[] args) {
        Connection connection;
        String host = "ec2-54-160-111-65.compute-1.amazonaws.com";
        int port = 32769;
        String id = "de3fd7e6-1713-4d9b-9ad7-9559168e6178";
        RethinkDB r = RethinkDB.r;
        connection = RethinkDB.r.connection().hostname(host).port(port).connect();
        String[] servicios = { "Peluqueria", "Paseo" };
        int a1, a2, m1, m2, d1, d2;
        Calendar c = Calendar.getInstance();

        c.set(2021, 2, 20);
        a1 = c.get(Calendar.YEAR);
        m1 = c.get(Calendar.MONTH) + 1;
        d1 = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_YEAR, 1);
        a2 = c.get(Calendar.YEAR);
        m2 = c.get(Calendar.MONTH) + 1;
        d2 = c.get(Calendar.DAY_OF_MONTH);
        ArrayList<Horario> query = r.db("ReyCanino").table("Horario")
                .filter(horario -> horario.getField("tiendaCanina").eq("f27d9717-9f4c-4ad2-ae36-8e9117b3848e"))
                .filter(horario -> horario.getField("servicio").eq("peluqueria"))
                .filter(horario -> horario.g("fi").during(r.time(a1, m1, d1, "Z"), r.time(a2, m2, d2, "Z")))
                .filter(horario -> horario.g("reserva").eq(null)).orderBy("fi").run(connection, Horario.class);
        ArrayList<Horario> l = new ArrayList<Horario>();

        System.out.println("-----------");
        for (int i = 0; i < query.size(); i++) {
            l.add(Util.convertToPojo(query.get(i), Optional.of(Horario.class)));
            System.out.println(l.get(i));
        }

        System.out.println();
        connection.close();
    }
}
