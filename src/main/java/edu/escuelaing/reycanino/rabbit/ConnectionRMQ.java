package edu.escuelaing.reycanino.rabbit;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ConnectionRMQ {
    private static final Logger LOG = Logger.getLogger("edu.escuelaing.reycanino.rabbitmq.ConnectionRMQ");
    private static ConnectionFactory factory = new ConnectionFactory();;
    private static Connection connection;
    private static Channel channel;

    public ConnectionRMQ() {
        factory.setHost(ConfigurationRMQ.RABBITMQ_SERVER);
        factory.setPort(ConfigurationRMQ.RABBITMQ_PORT);
        factory.setUsername(ConfigurationRMQ.USERNAME);
        factory.setPassword(ConfigurationRMQ.SECRET);
    }

    public static Channel create() {
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(ConfigurationRMQ.QUEUE_NAME, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            LOG.log(Level.INFO, e.getLocalizedMessage());
        }
        return channel;
    }

    void close() {
        try {
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            LOG.log(Level.INFO, e.getLocalizedMessage());
        }
    }
}