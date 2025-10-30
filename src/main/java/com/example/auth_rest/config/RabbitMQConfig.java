package com.example.auth_rest.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "auth-exchange";
    public static final String ROUTING_KEY_USER_CREATED = "user.created";
    public static final String ROUTING_KEY_USER_DELETED = "user.deleted";

    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
}
