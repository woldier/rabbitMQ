package com.woldier.producer.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static com.woldier.producer.config.RabbitmqConfig.EXCHANGE_NAME;

@org.springframework.stereotype.Service
public class RabbitService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    public void exc(){
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,"woldier.ff.xx","hello boot rabbit");
    }
}
