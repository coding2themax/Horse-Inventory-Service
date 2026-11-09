package com.coding2themax.horse.horseinventoryservice.saga;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Declares the saga exchanges and the inventory command queue. The settings
 * must match horse-store-order-service, or RabbitMQ refuses the declaration.
 */
@Configuration
public class SagaMessagingConfig {

  @Bean
  TopicExchange sagaExchange() {
    return new TopicExchange(SagaRoutes.EXCHANGE, true, false);
  }

  @Bean
  FanoutExchange sagaDeadLetterExchange() {
    return new FanoutExchange(SagaRoutes.DEAD_LETTER_EXCHANGE, true, false);
  }

  @Bean
  Queue sagaDeadLetterQueue() {
    return QueueBuilder.durable(SagaRoutes.DEAD_LETTER_QUEUE).build();
  }

  @Bean
  Binding sagaDeadLetterBinding() {
    return BindingBuilder.bind(sagaDeadLetterQueue()).to(sagaDeadLetterExchange());
  }

  @Bean
  Queue inventoryCommandsQueue() {
    return QueueBuilder.durable(SagaRoutes.INVENTORY_COMMANDS_QUEUE)
        .deadLetterExchange(SagaRoutes.DEAD_LETTER_EXCHANGE)
        .build();
  }

  @Bean
  Binding inventoryCommandsBinding() {
    return BindingBuilder.bind(inventoryCommandsQueue()).to(sagaExchange()).with("inventory.*");
  }

  /** JSON messages, typed by the listener's parameter rather than a class-name header. */
  @Bean
  MessageConverter sagaMessageConverter(ObjectMapper objectMapper) {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
    converter.setAlwaysConvertToInferredType(true);
    return converter;
  }
}
