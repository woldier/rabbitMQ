package com.woldier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.woldier.config.PublisherConfirmConfig.EXCHANGE_NAME;

/**
 * 确认机制测试类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ConfirmTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 测试confirm模式
     * 需要在yml配置文件中设置 spring.rabbitmq.publisherConfirms 为 true
     * 消息从 producer 到 exchange 无论如何都会回调confirmCallback 。
     */
    @Test
    public void test1(){

        /**
         * 设置一个confirm回调
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 相关数据
             * @param b 为true表示提交到交换机成功 为false表示到交换机失败
             * @param s 如果提交到交换机失败了的话 这里会给出错误信息
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {

                //回调得到
                System.out.println("correlationData = " + correlationData + ", b = " + b + ", s = " + s);
            }
        });
        /**
         * 这里我们模拟一个到exchange的异常 当前exchange的name 是不存在的
         */


        rabbitTemplate.convertAndSend(EXCHANGE_NAME+"1","woldier.1","confirm advance",new CorrelationData());
    }

    /**
     * 测试return模式
     * 需要在yml配置文件中设置 spring.rabbitmq.publisherReturns 为 true
     * 消息从 exchange-->queue 投递失败则会返回一个 returnCallback
     * 在rabbitTemplate中开启了rabbitTemplate.setMandatory(true)
     * 给出回调函数
     *
     *
     */
    @Test
    public void test2(){
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("message = " + message + ", replyCode = " + replyCode + ", replyText = " + replyText + ", exchange = " + exchange + ", routingKey = " + routingKey);
            }
        });


        /**
         * 这里模拟路由错误 我们绑定的路由是woldier.# 但是我们这里给定为some1.aaa 无法路由到 会进行回调 (注:路由成功不会进行回调)
         */
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,"some1.aaa");
    }
}
