package com.spendsmart.budget.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BUDGET_EXCHANGE = "budget.exchange";
    public static final String BUDGET_ROUTING_KEY = "budget.alert";
    public static final String BUDGET_QUEUE = "budget.alerts.queue";

    @Bean
    public DirectExchange budgetExchange() {
        return new DirectExchange(BUDGET_EXCHANGE);
    }

    @Bean
    public Queue budgetQueue() {
        return new Queue(BUDGET_QUEUE);
    }

    @Bean
    public Binding budgetBinding(Queue budgetQueue, DirectExchange budgetExchange) {
        return BindingBuilder.bind(budgetQueue).to(budgetExchange).with(BUDGET_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
