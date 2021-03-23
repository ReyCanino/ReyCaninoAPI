package edu.escuelaing.reycanino.db;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

public class RethinkDBConnectionFactory {
    private static String host;
    private static int port;

    public RethinkDBConnectionFactory(String host, int port) {
        RethinkDBConnectionFactory.host = host;
        RethinkDBConnectionFactory.port = port;
    }

    public static Connection createConnection() {
        return RethinkDB.r.connection().hostname(host).port(port).connect();
    }
}