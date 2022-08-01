 # 死信队列

队列绑定死信交换机： 给队列设置参数： x-dead-letter-exchange 和 x-dead-letter-routing-key

![image-20220801160424444](C:\Users\wang1\IdeaProjects\rabiitMQ\rabbitMQ-SpringBoot-Advance\rabbitMQ-05-dle\readme.assets\image-20220801160424444.png)

1. 死信交换机和死信队列和普通的没有区别 
2. 当消息成为死信后，如果该队列绑定了死信交换机，则消息会被死信交换机重新路由到死信队列 
3. 消息成为死信的三种情况： 
   1. 队列消息长度到达限制； 
   2. 消费者拒接消费消息，并且不重回队列； 
   3. 原队列存在消息过期设置，消息到达超时时间未被消费；









coding(第一种情况(超过最大)和第三种情况(超时)的模拟)

```java
package com.woldier.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息可靠传递所使用的队列及交换机的声明与绑定
 */
@Configuration
public class PublisherConfirmConfig {
    public static final String EXCHANGE_NAME = "boot-advance-dle";
    public static final String DEAD_EXCHANGE_NAME = "boot-advance-dle-dead";
    public static final String QUEUE_NAME = "boot-advance-dle-topic-queue";
    public static final String DEAD_QUEUE_NAME = "boot-advance-dle-dead-topic-queue";
    private static final String ROUTING_KEY = "woldier.#";
    private static final String DEAD_ROUTING_KEY = "woldier.dead.#";
    //ttl时间(毫秒)
    private static final int TTL_EXPIRATION = 20000;
    /*设置为更大*/
    //private static final int TTL_EXPIRATION = 55000;

    /**
     * 交换机
     */
    @Bean("bootExchange")
    public Exchange bootExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("bootExchangeDead")
    public Exchange bootExchangeDead() {
        return ExchangeBuilder.topicExchange(DEAD_EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 队列 ,
     */
    @Bean("bootQueue") //Bean没有指定名字 则默认为方法名
    public Queue bootQueue() {
        /**
         * 设置过期参数
         * 绑定死信交换机
         * 绑定死信routingkey(在死信消息发布过程中 我们可以看作是一个publisher)
         */
        return QueueBuilder.durable(QUEUE_NAME).

                withArgument("x-message-ttl", TTL_EXPIRATION).
                /*
                How many (ready) messages a queue can contain before it starts to drop them from its head.(Sets the "x-max-length" argument.)
                设置queue最大容量,超过则会drop队顶数据*/
                withArgument("x-max-length",10).
                /*死信交换机设置*/
                withArgument("x-dead-letter-exchange", DEAD_EXCHANGE_NAME).
                /*死信路由设置*/
                withArgument("x-dead-letter-routing-key", "woldier.dead.111")
                .build();
    }

    /*死信队列*/
    @Bean("bootQueueDead") //Bean没有指定名字 则默认为方法名
    public Queue bootQueueDead() {
        return QueueBuilder.durable(DEAD_QUEUE_NAME).build();
    }

    /**
     * 绑定
     */

    @Bean
    public Binding bindQueue2Exchange(
            @Qualifier("bootExchange") Exchange bootExchange, //若容器中有多个exchange可以使用变量名代表要注入哪一个
            @Qualifier("bootQueue") Queue queue //也可以使用@Qualifier指定
    ) {
        /*绑定并设置路由信息*/
        return BindingBuilder.bind(queue).to(bootExchange).with(ROUTING_KEY).noargs();
    }

    /*死信交换机与queue绑定*/
    @Bean
    public Binding bindQueue2ExchangeDead(
            @Qualifier("bootExchangeDead") Exchange bootExchange, //若容器中有多个exchange可以使用变量名代表要注入哪一个
            @Qualifier("bootQueueDead") Queue queue //也可以使用@Qualifier指定
    ) {
        /*绑定并设置路由信息*/
        return BindingBuilder.bind(queue).to(bootExchange).with(DEAD_ROUTING_KEY).noargs();
    }
}

```

我们设置容量最大为10,此时设置的队列过期时间为20,000 ms(20s) 

- 我们发送二十条消息且间隔较短时sleep(50) 可以测试队满的情况

```java
package com.woldier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.woldier.config.PublisherConfirmConfig.EXCHANGE_NAME;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitPub05 {

    final static int NUM = 20;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 发送消息
     */
    @Test
    public void test1() throws InterruptedException {
        for(int i=0;i<NUM;i++){
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,"woldier.dle","hello  dle "+i);
        Thread.sleep(50);
        }
    }



}

```



![image-20220801163451242](C:\Users\wang1\IdeaProjects\rabiitMQ\rabbitMQ-SpringBoot-Advance\rabbitMQ-05-dle\readme.assets\image-20220801163451242.png)

看到下面的queue只有十条 剩余的数据通过死信交换机到达了死信路由(由于之前测试时死信队列加入了10条数据,所以现在看到了有20)清除原所有的数据再次测试

![image-20220801163827966](C:\Users\wang1\IdeaProjects\rabiitMQ\rabbitMQ-SpringBoot-Advance\rabbitMQ-05-dle\readme.assets\image-20220801163827966.png)

- 继续等待,可以观察到过期情况

等待设置的队列过期时间后可以观察到

![image-20220801163617502](C:\Users\wang1\IdeaProjects\rabiitMQ\rabbitMQ-SpringBoot-Advance\rabbitMQ-05-dle\readme.assets\image-20220801163617502.png)

(由于之前测试时死信队列加入了10条数据,所以现在看到了有20)清除原所有的数据再次测试

![image-20220801163858154](C:\Users\wang1\IdeaProjects\rabiitMQ\rabbitMQ-SpringBoot-Advance\rabbitMQ-05-dle\readme.assets\image-20220801163858154.png)



- 测试消费者拒收消息,且不妨会队列的情况

此情况下要开启手动确认,这里还设置了qos

```yaml
spring:
  rabbitmq:
    host: 192.168.59.130
    username: woldier
    password: woldier
    virtual-host: /woldier
    port: 5672
    listener:
      simple:
        #开启ack 为手动
        acknowledge-mode: manual
        #设置预取条数为1(设置了与其必须设置手动确认 不然rabbit 本fetch消费完成之后不会发新的fetch)
        prefetch: 1
```

```java
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
```

