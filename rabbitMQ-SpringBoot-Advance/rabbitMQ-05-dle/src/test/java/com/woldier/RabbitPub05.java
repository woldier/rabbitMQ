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

    final static int NUM = 10;
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
