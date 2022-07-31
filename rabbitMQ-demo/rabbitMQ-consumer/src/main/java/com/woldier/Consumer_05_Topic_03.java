package com.woldier;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Routing(topic)
 * https://www.rabbitmq.com/tutorials/tutorial-five-java.html
 */
public class Consumer_05_Topic_03 {
    final static String EXCHANGE_NAME = "test_topic";
    /**
     * 注意error.# 可以匹配error.ksksk.ksk 可以是长度为n的
     */
//    final static String ROUTING_KEY[] = {"info.*","warn.woldier","error.#"};
    final static String QUEUE_NAME[] = {"topic_info_*_queue","topic_warn_woldier_queue","topic_error_#_queue"};
    final static int QUEUE_POS = 2;
    /**
     * 步骤如下
     * 1.创建连接工厂
     * 2.设置连接工厂连接参数
     * 3.通过工厂对象创建连接
     * 4.创建channel
     * 5.channel声明exchange ,创建queue
     * 6.queue与exchange绑定
     * 7.接收消息
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        // * 1.创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        // * 2.设置连接参数
        factory.setHost("192.168.59.130");//设置ip
        factory.setPort(5672); //设置端口,管理页面中可以查看
        factory.setVirtualHost("/woldier");//设置虚拟机
        factory.setUsername("woldier");
        factory.setPassword("woldier");
        // * 3.通过工厂对象创建连接
        Connection connection = factory.newConnection();
        // * 4.设置channel
        Channel channel = connection.createChannel();


        //* 6.接收消息
        /**
        Start a non-nolocal, non-exclusive consumer, with a server-generated consumerTag and specified arguments. Provide access to basic.deliver, basic.cancel and shutdown signal callbacks (which is sufficient for most cases). See methods with a Consumer argument to have access to all the application callbacks.
        Params:
        queue – the name of the queue
        autoAck – true if the server should consider messages acknowledged once delivered; false if the server should expect explicit acknowledgements
        arguments – a set of arguments for the consume
        deliverCallback – callback when a message is delivered
        cancelCallback – callback when the consumer is cancelled
        shutdownSignalCallback – callback when the channel/connection is shut down
        Returns:
        the consumerTag generated by the server
        Throws:
        IOException – if an error is encountered
        Since:
        5.0
        See Also:
        AMQP.Basic.Consume, AMQP.Basic.ConsumeOk, basicAck, basicConsume(String, boolean, String, boolean, boolean, Map, Consumer)
         */
        //String basicConsume(String queue, boolean autoAck, Map<String, Object> arguments, DeliverCallback deliverCallback, CancelCallback cancelCallback, ConsumerShutdownSignalCallback shutdownSignalCallback) throws IOException;
        DefaultConsumer consumer = new DefaultConsumer(channel){
            /**
             * 实现回调方法 (原类中的回调方法为空实现)
             * @param consumerTag
             * @param envelope
             * @param properties
             * @param body
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                try {
                    System.out.println("body = " +new String(body));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //auto ack 设置为True 会自动确认 ,设置为false需要手动进行确认 在handleDelivery中进行确认
                    //channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        //channel.basicQos(1);//设置每次只取一条数据
        //auto ack 设置为True 会自动确认 ,设置为false需要手动进行确认 在handleDelivery中进行确认
        channel.basicConsume(QUEUE_NAME[QUEUE_POS],true,consumer);

        /**
         * 最后不需要关闭channel 和 connection 因为需要持续监听
         */
        while (true);
    }
}
