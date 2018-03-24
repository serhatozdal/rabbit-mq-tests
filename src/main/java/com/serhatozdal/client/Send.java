package com.serhatozdal.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author serhatozdal
 */
public class Send {

    private static final String QUEUE_NAME = "analitik";

    public static void main(String[] args) throws IOException, TimeoutException {

        String jsonFile = new Send().getFile("visits_out.json");

        JSONArray jsonArray = new JSONArray(jsonFile);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("");
        factory.setUsername("");
        factory.setPassword("");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        int i = 0;
        while (i != 1000) {
            ++i;
            for (int j = 0; j < jsonArray.length(); j++) {
                String json = jsonArray.get(j).toString();

                channel.basicPublish("", QUEUE_NAME, null, json.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + json + "'");
            }
        }

        channel.close();
        connection.close();
    }

    private String getFile(String fileName) {
        StringBuilder result = new StringBuilder();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }
}
