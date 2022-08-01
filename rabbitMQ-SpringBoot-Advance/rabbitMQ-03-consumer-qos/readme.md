消费端限流

# 1.导入pom

# 2.配置yml 开启手动确认 并设置 prefech=1

```yaml
spring:
  rabbitmq:
    host: 192.168.59.130
    username: woldier
    password: woldier
    virtual-host: /woldier
    port: 5672
    #开启ack 为手动
    listener:
      direct:
        acknowledge-mode: manual

```

# 3. 配置pub
exchange , queue ,bind 
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
    public static final String EXCHANGE_NAME = "boot-advance-qos";
    public static final String QUEUE_NAME = "boot-advance-qos-topic-queue";
    private static final String ROUTING_KEY = "woldier.#";

    /**
     * 交换机
     */
    @Bean("bootExchange")
    public Exchange bootExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }
    /**
     * 队列 ,
     */
    @Bean("bootQueue") //Bean没有指定名字 则默认为方法名
    public Queue bootQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }
    /**
     * 绑定
     */

    @Bean
    public Binding bindQueue2Exchange(
            @Qualifier("bootExchange") Exchange bootExchange, //若容器中有多个exchange可以使用变量名代表要注入哪一个
            @Qualifier("bootQueue") Queue queue //也可以使用@Qualifier指定
    ){
        /*绑定并设置路由信息*/
        return BindingBuilder.bind(queue).to(bootExchange).with(ROUTING_KEY).noargs();
    }
}

```

# 4.发起消息
```java

package com.woldier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.woldier.config.PublisherConfirmConfig.EXCHANGE_NAME;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitPub {

    final static int NUM = 10;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 发送消息
     */
    @Test
    public void test1(){
        for(int i=0;i<NUM;i++){
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,"woldier.qos","hello  qos "+i);
        }
    }
}

```

# 5.接收消息

```java

@Configuration
public class ConsumerConfig {

    /**
     * 监听01中的queue
     * @param message
     * @param channel
     */
    @RabbitListener(queues = "boot-advance-qos-topic-queue") //与pub设置的一致
    public void listen(Message message , Channel channel) throws IOException {
        System.out.println("message = " + message + ", channel = " + channel);
        try {
            /*事务操作*/
            Thread.sleep(10000);
            /*成功ack*/
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }
        catch (Exception e){
            /*添加错误ack 参数分别为 tag , multiple , requeue */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
        }
    }
}
```