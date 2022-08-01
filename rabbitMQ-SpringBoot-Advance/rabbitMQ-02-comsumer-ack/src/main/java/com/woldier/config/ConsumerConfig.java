package com.woldier.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
//@RabbitListener
//public class ConsumerConfig {
//
//
//    /**
//     * 监听器
//     */
//    @RabbitHandler
//    public void listen(Message message , Channel channel){
//
//    }
//
//}


@Configuration
public class ConsumerConfig {

    /**
     * 监听01中的queue
     * @param message
     * @param channel
     */
    @RabbitListener(queues = "boot-confirm-topic-queue-advance")
    public void listen(Message message , Channel channel) throws IOException {
        System.out.println("message = " + message + ", channel = " + channel);
        try {
            /*事务操作*/
                int i= 3/0;
            /*成功ack*/
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }
        catch (Exception e){
            /*添加错误ack 参数分别为 tag , multiple , requeue */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
        }
    }
}