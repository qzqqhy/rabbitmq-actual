package com.demo.connection;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConnection {
    private static String userName = "root";
    private static String password = "root";
    private static String virtualHost = "/";
    private static String IP_Address = "127.0.0.1";
    private static int port = 5672;
    private static String RabbitMQConnection_exchange = "RabbitMQConnection_exchange";
    private static String RabbitMQConnection_routingKey = "RabbitMQConnection_routingKey";


    //TODO 代码清单 3-7

    /**
     * 创建了一个持久化的、非自动删除的、绑定类型为 direct 的交换器 <br />
     * 同时也创建了一 个非持久化的、排他的、自动删除的队列(此队列的名称由 RabbitMQ 自动生成)
     * @param channel
     * @throws IOException
     */
    static void declareExchange(Channel channel) throws IOException {
        channel.exchangeDeclare(RabbitMQConnection_exchange, "direct", true);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,RabbitMQConnection_exchange,RabbitMQConnection_routingKey);
    }

    static class Connection_1 {
        public static void main(String[] args) throws IOException, TimeoutException {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername(userName);
            factory.setPassword(password);
            factory.setVirtualHost(virtualHost);
            factory.setHost(IP_Address);
            factory.setPort(port);
            Connection connection = factory.newConnection();
            System.out.println(connection);

            Channel channel = connection.createChannel();

//            declareExchange(channel);
        }
    }

    static class Connection_2 {
        public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqp://root:root@127.0.0.1:5672/my_vhost");
            // actory.setUri("amqp://root:root@127.0.0.1:5672"); 这两种都可以 virtualHost为 / 的话 不需要填
            // factory.setUri ("amqp://userName:password@ipAddress:portNumber/virtualHost”);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            System.out.println(channel);

            declareExchange(channel);
        }
    }
}
