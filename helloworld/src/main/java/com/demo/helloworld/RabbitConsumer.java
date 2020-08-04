package com.demo.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 5672;//RabbitMQ 默认端口

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Address[] addresses = new Address[]{
                new Address(IP_ADDRESS, PORT)
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("root");
        factory.setPassword("root");
        //这里的连接方式与生产者的demo略有不同，注意辨别区别
        Connection connection = factory.newConnection(addresses);//创建连接

        final Channel channel = connection.createChannel();//创建信道

        channel.basicQos(64); // 设置客户端最多接收未被ack的消息个数

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException {
                // no work to do
                System.out.println("recv message : " + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        channel.basicConsume(QUEUE_NAME,consumer);

        //等待回调函数执行完毕后，关闭资源
        TimeUnit.SECONDS.sleep(5);

        channel.close();
        connection.close();


    }
}
