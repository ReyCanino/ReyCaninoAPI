package edu.escuelaing.reycanino.rabbitMQ;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionRMQ {
    private static ConnectionFactory factory = null;
    private static Connection connection;
    private static Channel channel;

    public ConnectionRMQ() {
        factory = new ConnectionFactory();
        factory.setHost(ConfigurationRMQ.RABBITMQ_SERVER);
        factory.setPort(ConfigurationRMQ.RABBITMQ_PORT);
        factory.setUsername(ConfigurationRMQ.USERNAME);
        factory.setPassword(ConfigurationRMQ.PASSWORD);
    }

    public static Channel create() {
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(ConfigurationRMQ.QUEUE_NAME, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return channel;
    }

    void close() {
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}