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
    public static final String EXCHANGE_NAME = "boot-advance-ttl";
    public static final String QUEUE_NAME = "boot-advance-ttl-topic-queue";
    private static final String ROUTING_KEY = "woldier.#";
    //ttl时间(毫秒)
    private static final int TTL_EXPIRATION = 10000;
    /*设置为更大*/
    //private static final int TTL_EXPIRATION = 55000;
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
        /**
         * 设置过期参数
         */
        return QueueBuilder.durable(QUEUE_NAME).withArgument("x-message-ttl",TTL_EXPIRATION).build();
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
