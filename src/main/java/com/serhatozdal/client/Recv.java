package com.serhatozdal.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import com.serhatozdal.client.model.UserVisit;

import java.io.IOException;

/**
 * @author serhatozdal
 */
public class Recv {

    private final static String QUEUE_NAME = "analitik";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("");
        factory.setUsername("");
        factory.setPassword("");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String json = new String(body, "UTF-8");

                ObjectMapper mapper = new ObjectMapper();
                UserVisit userVisit = mapper.readValue(json, UserVisit.class);

                System.out.println(" [x] Received '" + json + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}