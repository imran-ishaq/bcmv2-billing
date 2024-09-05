package com.itmaxglobal.billing.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.billing-queue}")
    private String billingQueue;

    @Value("${rabbitmq.billing-routing-key}")
    private String billingRoutingKey;

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue billingQueue(){
        return new Queue(billingQueue);
    }

    @Bean
    public Binding bindingConnectedQueue(){
        return BindingBuilder
                .bind(billingQueue())
                .to(directExchange())
                .with(billingRoutingKey);
    }

    @Bean
    public MessageConverter convertor(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(convertor());
        return rabbitTemplate;
    }

}
