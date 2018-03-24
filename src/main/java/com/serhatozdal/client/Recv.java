package com.serhatozdal.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import com.serhatozdal.client.model.UserVisit;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

        SqlConnection sc = new SqlConnection();
        final java.sql.Connection sqlConnection = sc.getConnection();

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String json = new String(body, "UTF-8");

                ObjectMapper mapper = new ObjectMapper();
                UserVisit userVisit = mapper.readValue(json, UserVisit.class);

                try {
                    String sql = "INSERT INTO test.user_visit_test(visit_id, site_id, action_name, url, random_value, " +
                            "referrer_url, resolution, hour, minute, second, user_agent, language, flash, java, quick_time" +
                            "real_player, pdf, windows_media, silverlight, ip_address VALUES" +
                            "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    PreparedStatement psmt = sqlConnection.prepareStatement(sql);
                    psmt.setString(1, userVisit.getVisiId());
                    psmt.setString(2, userVisit.getIdsite());
                    psmt.setString(3, userVisit.getActionName());
                    psmt.setString(4, userVisit.getUrl());
                    psmt.setString(5, userVisit.getRandomValue());
                    psmt.setString(6, userVisit.getRefererUrl());
                    psmt.setString(7, userVisit.getResolution());
                    psmt.setInt(8, userVisit.getHour());
                    psmt.setInt(9, userVisit.getMinute());
                    psmt.setInt(10, userVisit.getSecond());
                    psmt.setString(11, userVisit.getUserAgent());
                    psmt.setString(12, userVisit.getLanguage());
                    psmt.setInt(13, userVisit.getPlugin().getFlash());
                    psmt.setInt(14, userVisit.getPlugin().getJava());
                    psmt.setInt(15, userVisit.getPlugin().getQuicktime());
                    psmt.setInt(16, userVisit.getPlugin().getRealPlayer());
                    psmt.setInt(17, userVisit.getPlugin().getPdf());
                    psmt.setInt(18, userVisit.getPlugin().getWindowsMedia());
                    psmt.setInt(19, userVisit.getPlugin().getSilverlight());
                    psmt.setString(20, userVisit.getIpAdress());

                    psmt.executeQuery();
                    psmt.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.println(" [x] Received '" + json + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
        sc.close();

    }
}