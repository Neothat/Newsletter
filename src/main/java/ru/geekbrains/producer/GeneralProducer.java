package ru.geekbrains.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class GeneralProducer {

    private static final String EXCHANGER = "news_exchanger";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGER, BuiltinExchangeType.DIRECT);

            while (true) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                String[] arrayMessage = message.split("\\s+");
                String newMessage = Arrays.stream(arrayMessage).skip(1).collect(Collectors.joining(" "));

                channel.basicPublish(EXCHANGER, arrayMessage[0], null, newMessage.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
