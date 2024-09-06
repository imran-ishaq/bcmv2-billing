package com.itmaxglobal.billing.configs;

import com.itmaxglobal.billing.consumer.BillingConsumer;
import com.itmaxglobal.billing.dto.BillingStatusRequestDTO;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.Bidi;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.billing-queue}")
    private String billingQueue;

    @Value("${rabbitmq.billing-routing-key}")
    private String billingRoutingKey;

    @Value("${rabbitmq.concurrency-threads}")
    private int concurrencyThreads;

    @Value("${rabbitmq.max-concurrency-threads}")
    private int maxConcurrencyThreads;

    @Value("${rabbitmq.prefetch-count}")
    private int prefetchCount;

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue billingQueue(){
        return QueueBuilder.durable(billingQueue)
                .build();
    }

    @Bean
    public Binding bindingBillingQueue(){
        return BindingBuilder
                .bind(billingQueue())
                .to(directExchange())
                .with(billingRoutingKey);
    }

    @Bean
    public MessageConverter convertor(){
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(new DefaultJackson2JavaTypeMapper() {
            @Override
            public Map<String, Class<?>> getIdClassMapping() {
                Map<String, Class<?>> idClassMapping = new HashMap<>();
                idClassMapping.put("com.example.bcm_cddm.dtos.request.BillingStatusRequestDTO", BillingStatusRequestDTO.class);
                return idClassMapping;
            }
        });
        return converter;
    }

    @Bean("rabbitListenerContainerFactory")
    public RabbitListenerContainerFactory<?> rabbitFactory
            (ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(convertor());
        factory.setDefaultRequeueRejected(false);
        factory.setConcurrentConsumers(concurrencyThreads);
        factory.setMaxConcurrentConsumers(maxConcurrencyThreads);
        factory.setPrefetchCount(prefetchCount);
        return factory;
    }

}
