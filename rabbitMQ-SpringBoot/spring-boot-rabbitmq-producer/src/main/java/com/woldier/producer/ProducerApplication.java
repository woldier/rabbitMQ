package com.woldier.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;

@SpringBootApplication
public class ProducerApplication {
    public static void main(String[] args) throws InterruptedException {
        ConfigurableEnvironment environment = SpringApplication.run(ProducerApplication.class, args).getEnvironment();
    }
}
