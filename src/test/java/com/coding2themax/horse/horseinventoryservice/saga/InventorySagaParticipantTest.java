package com.coding2themax.horse.horseinventoryservice.saga;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.AmqpTemplate;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseProduct;
import com.coding2themax.horse.horseinventoryservice.service.FeedStockService;

class InventorySagaParticipantTest {

  private AmqpTemplate amqp;
  private FeedStockService feedStock;
  private InventorySagaParticipant participant;

  @BeforeEach
  void setUp() {
    amqp = mock(AmqpTemplate.class);
    feedStock = new FeedStockService(List.of(
        new HorseProduct("hay", "Hay", FeedStockService.FEED_CATEGORY, 10, 18.5, 10.0)));
    participant = new InventorySagaParticipant(feedStock, amqp);
  }

  @Test
  void reserveRepliesWithPrice() {
    participant.onCommand(command("hay", 3), SagaRoutes.INVENTORY_RESERVE);

    FeedPurchaseMessage reply = reply(SagaRoutes.INVENTORY_RESERVED);
    assertThat(reply.unitPrice()).isEqualByComparingTo("18.50");
    assertThat(reply.totalPrice()).isEqualByComparingTo("55.50");
  }

  @Test
  void reserveRepliesRejectedWhenOutOfStock() {
    participant.onCommand(command("hay", 50), SagaRoutes.INVENTORY_RESERVE);

    assertThat(reply(SagaRoutes.INVENTORY_REJECTED).failureReason()).contains("in stock");
  }

  @Test
  void commitRepliesCommitted() {
    FeedPurchaseMessage command = command("hay", 3);
    feedStock.reserve(command.sagaId(), "hay", 3);

    participant.onCommand(command, SagaRoutes.INVENTORY_COMMIT);

    reply(SagaRoutes.INVENTORY_COMMITTED);
  }

  @Test
  void releaseRestoresStockAndKeepsFailureReason() {
    FeedPurchaseMessage command = command("hay", 3).withFailure("Card declined");
    feedStock.reserve(command.sagaId(), "hay", 3);

    participant.onCommand(command, SagaRoutes.INVENTORY_RELEASE);

    assertThat(reply(SagaRoutes.INVENTORY_RELEASED).failureReason()).isEqualTo("Card declined");
    assertThat(feedStock.getFeedStock().get(0).getStock()).isEqualTo(10);
  }

  @Test
  void unknownCommandIsRejected() {
    assertThatThrownBy(() -> participant.onCommand(command("hay", 1), "inventory.unknown"))
        .isInstanceOf(IllegalArgumentException.class);
    verifyNoInteractions(amqp);
  }

  private static FeedPurchaseMessage command(String feedId, int quantity) {
    return new FeedPurchaseMessage(UUID.randomUUID(), "cust-1", feedId, quantity, "tok_visa",
        null, null, null, null);
  }

  private FeedPurchaseMessage reply(String routingKey) {
    ArgumentCaptor<FeedPurchaseMessage> captor = ArgumentCaptor.forClass(FeedPurchaseMessage.class);
    verify(amqp).convertAndSend(eq(SagaRoutes.EXCHANGE), eq(routingKey), captor.capture());
    return captor.getValue();
  }
}
