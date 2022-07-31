package com.woldier.producer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Pub&&Sub
 * https://www.rabbitmq.com/tutorials/tutorial-three-java.html
 */
public class Producer_03_PubSub {

    /**
     * 步骤如下
     * 1.创建连接工厂
     * 2.设置连接工厂连接参数
     * 3.通过工厂对象创建连接
     * 4.创建channel
     * 5.channel声明exchange
     * 6.发送消息
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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
        // * 5.设置exchange
        /**
         * Declare an exchange, via an interface that allows the complete set of arguments. 通过允许完整参数集的接口声明交换
         * Params:
         * exchange – the name of the exchange 交易所的名称
         * type – the exchange type 交换类型
         * durable – true if we are declaring a durable exchange (the exchange will survive a server restart) 如果我们声明了一个持久交换，则为true(该交换在服务器重启后仍然有效)
         * autoDelete – true if the server should delete the exchange when it is no longer in use 如果服务器应该在不再使用交换器时删除该交换器，则为true
         * internal – true if the exchange is internal, i.e. can't be directly published to by a client. 如果交换是内部的，即不能被客户端直接发布到
         * arguments – other properties (construction arguments) for the exchange 交换的其他属性(构造参数)
         * Returns:
         * a declaration-confirm method to indicate the exchange was successfully declared 确认方法，表示交换已成功声明
         * Throws:
         * IOException – if an error is encountered
         * See Also:
         * AMQP.Exchange.Declare, AMQP.Exchange.DeclareOk
         */
        //exchangeDeclare(String exchange, BuiltinExchangeType type,boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments) throws IOException;
        channel.exchangeDeclare("test_fanout", BuiltinExchangeType.FANOUT, true, true, false, null);


        // * 6.发送消息
        /**
         Publish a message. Publishing to a non-existent exchange will result in a channel-level protocol exception, which closes the channel. Invocations of Channel#basicPublish will eventually block if a resource-driven alarm  is in effect.
         发布一条消息。发布到不存在的交换将导致通道级协议异常，从而关闭通道。如果资源驱动的告警生效，通道#basicPublish的调用将最终被阻塞
         Params:
         exchange – the exchange to publish the message to 将消息发布到的交换器 ,简单模式下为默认的给空字符就可以了
         routingKey – the routing key 路由键
         mandatory – true if the 'mandatory' flag is to be set 如果要设置' Mandatory '标志，则为true
         immediate – true if the 'immediate' flag is to be set. Note that the RabbitMQ server does not support this flag
         如果要设置' Immediate '标志，则为true。注意RabbitMQ服务器不支持这个标志
         props – other properties for the message - routing headers etc 消息的其他属性—路由标题等
         body – the message body 消息正问
         Throws:
         IOException – if an error is encountered
         See Also:
         AMQP.Basic.Publish, Resource-driven alarms
         */
        String msg = "hello rabbit fanout";
        //void basicPublish(String exchange, String routingKey, boolean mandatory, boolean immediate, BasicProperties props, byte[] body)throws IOException;
        channel.basicPublish("test_fanout", "", null, msg.getBytes());




        /*
        释放资源
         */
        channel.close();
        connection.close();
    }
}
