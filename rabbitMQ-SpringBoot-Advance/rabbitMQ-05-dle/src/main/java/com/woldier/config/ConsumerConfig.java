package com.woldier.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class ConsumerConfig {

    /**
     * 当前prefetch为1,只有确认后才能得到下一条消息
     * 当前模拟一个 返回失败ack
     * @param message
     * @param channel
     */
    @RabbitListener(queues = "boot-advance-dle-topic-queue") //与pub设置的一致
    public void listen(Message message , Channel channel) throws IOException {
        System.out.println("message = " + message + ", channel = " + channel);
        try {
            /*事务操作*/

            int i = 1/0;
            /*成功ack*/
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }
        catch (Exception e){
            /*添加错误ack 参数分别为 tag , multiple , requeue */
            /**
             * 这里设置requeue为false 此消息就会被加入到死信队列
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
        }
    }
}