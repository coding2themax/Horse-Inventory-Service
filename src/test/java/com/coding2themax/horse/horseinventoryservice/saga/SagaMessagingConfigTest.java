package com.coding2themax.horse.horseinventoryservice.saga;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class SagaMessagingConfigTest {

  private final SagaMessagingConfig config = new SagaMessagingConfig();

  @Test
  void inventoryQueueReceivesInventoryCommands() {
    Binding binding = config.inventoryCommandsBinding();

    assertThat(binding.getDestination()).isEqualTo(SagaRoutes.INVENTORY_COMMANDS_QUEUE);
    assertThat(binding.getExchange()).isEqualTo(SagaRoutes.EXCHANGE);
    assertThat(binding.getRoutingKey()).isEqualTo("inventory.*");
    assertThat(config.inventoryCommandsQueue().getArguments())
        .containsEntry("x-dead-letter-exchange", SagaRoutes.DEAD_LETTER_EXCHANGE);
  }

  @Test
  void failedMessagesGoToDeadLetterQueue() {
    Binding binding = config.sagaDeadLetterBinding();

    assertThat(binding.getDestination()).isEqualTo(SagaRoutes.DEAD_LETTER_QUEUE);
    assertThat(binding.getExchange()).isEqualTo(SagaRoutes.DEAD_LETTER_EXCHANGE);
    assertThat(config.sagaExchange().isDurable()).isTrue();
    assertThat(config.sagaDeadLetterExchange().isDurable()).isTrue();
  }

  @Test
  void converterReadsMessageWithoutTypeHeader() {
    MessageConverter converter = config.sagaMessageConverter(new ObjectMapper().registerModule(new JavaTimeModule()));
    FeedPurchaseMessage original = new FeedPurchaseMessage(UUID.randomUUID(), "cust-1", "hay", 2, "tok",
        new BigDecimal("18.50"), new BigDecimal("37.00"), null, null);

    Message message = converter.toMessage(original, new MessageProperties());
    message.getMessageProperties().getHeaders().remove("__TypeId__");
    message.getMessageProperties().setInferredArgumentType(FeedPurchaseMessage.class);

    assertThat(converter.fromMessage(message)).isEqualTo(original);
  }
}
