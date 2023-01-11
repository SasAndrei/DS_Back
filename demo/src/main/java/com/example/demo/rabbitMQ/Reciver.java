package com.example.demo.rabbitMQ;

import com.example.demo.controller.NotificationController;
import com.example.demo.dto.NotificationDTO;
import com.example.demo.model.OwnedDevice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//@Service
public class Reciver {

    private NotificationController notificationController;
    private static String TASK_QUEUE_NAME;
    private static String HOST_NAME;
    private static String USERNAME;
    private static String VIRTUAL_HOST_NAME;
    private static String PASSWORD;

    public Reciver(NotificationController measurementService) {
        this.notificationController = measurementService;
        this.TASK_QUEUE_NAME = "devices";
        this.HOST_NAME = "goose.rmq2.cloudamqp.com";
        this.USERNAME = "qkkgdxwt";
        this.VIRTUAL_HOST_NAME = "qkkgdxwt";
        this.PASSWORD = "ZfjzuqFpqq56-JCcMiPS9m7IqlBUr3xV";
    }

    private ConnectionFactory createConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.HOST_NAME);
        factory.setUsername(this.USERNAME);
        factory.setVirtualHost(this.VIRTUAL_HOST_NAME);
        factory.setPassword(this.PASSWORD);
        factory.setRequestedHeartbeat(30);
        factory.setConnectionTimeout(30000);
        return factory;
    }

    private void receiver(long thId) throws IOException, TimeoutException {
        ConnectionFactory factory = this.createConnection();
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(this.TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" Waiting for readings");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            ObjectMapper mapper = new ObjectMapper();
            ReadingRecive readingRecive = mapper.readValue(delivery.getBody(), ReadingRecive.class);
            try {
                doWork(readingRecive);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(this.TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });

    }

    private void sendSocketMessage(String deviceName, String client) {
        NotificationDTO notificationDTO = new NotificationDTO(client, "Maximum hourly energy consumption exceeded for device: " + deviceName);
        this.notificationController.sendMessage(notificationDTO);
    }


    private void doWork(ReadingRecive readingRecive) {
        Boolean overReading = this.notificationController.addFromRabbitMQ(readingRecive);
        OwnedDevice device = this.notificationController.getDevice(readingRecive.getId());
        System.out.println(readingRecive);

        if(overReading){
            sendSocketMessage(device.getDevice().getDescription(), device.getUser().getName());
        }
    }


    public void run() {
        try {
            receiver(Thread.currentThread().getId());
        } catch (Exception e) {
            System.out.println("Exception is caught");
        }
    }

}
