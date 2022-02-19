package ru.geekbrains.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class NewsletterConsumer {

    private static final String EXCHANGER = "news_exchanger";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGER, BuiltinExchangeType.DIRECT);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received '" + message + "'");
        };

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            String[] arrayMessage = command.split("\\s+");
            if (arrayMessage[0].equals("set_topic") && arrayMessage.length == 2) {

                String queueName = channel.queueDeclare().getQueue();

                channel.queueBind(queueName, EXCHANGER, arrayMessage[1]);

                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                });
            }
        }

    }


}
