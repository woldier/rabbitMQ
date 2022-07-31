package com.woldier.producer.test;

import com.woldier.producer.service.RabbitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.woldier.producer.config.RabbitmqConfig.EXCHANGE_NAME;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitTest {
    @Autowired
    RabbitService rabbitService;

    @Test
    public void test1(){
       rabbitService.exc();
    }
}
