package com.coding2themax.horse.horseinventoryservice.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.coding2themax.horse.horseinventoryservice.service.FeedStockService;
import com.coding2themax.horse.horseinventoryservice.service.FeedStockService.Reservation;

/**
 * The inventory step of the feed purchase saga. It reserves stock, commits it
 * once payment has gone through, or releases it if payment failed.
 */
@Component
public class InventorySagaParticipant {

  private static final Logger log = LoggerFactory.getLogger(InventorySagaParticipant.class);

  private final FeedStockService feedStock;
  private final AmqpTemplate amqp;

  public InventorySagaParticipant(FeedStockService feedStock, AmqpTemplate amqp) {
    this.feedStock = feedStock;
    this.amqp = amqp;
  }

  @RabbitListener(queues = SagaRoutes.INVENTORY_COMMANDS_QUEUE)
  public void onCommand(FeedPurchaseMessage command, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
    log.info("Saga {} received {}", command.sagaId(), routingKey);
    switch (routingKey) {
      case SagaRoutes.INVENTORY_RESERVE -> reserve(command);
      case SagaRoutes.INVENTORY_COMMIT -> {
        feedStock.commit(command.sagaId());
        reply(SagaRoutes.INVENTORY_COMMITTED, command);
      }
      case SagaRoutes.INVENTORY_RELEASE -> {
        feedStock.release(command.sagaId());
        reply(SagaRoutes.INVENTORY_RELEASED, command);
      }
      default -> throw new IllegalArgumentException("Unknown inventory command: " + routingKey);
    }
  }

  private void reserve(FeedPurchaseMessage command) {
    Reservation reservation = feedStock.reserve(command.sagaId(), command.feedId(), command.quantity());
    if (reservation.accepted()) {
      reply(SagaRoutes.INVENTORY_RESERVED, command.withPricing(reservation.unitPrice()));
    } else {
      reply(SagaRoutes.INVENTORY_REJECTED, command.withFailure(reservation.rejectionReason()));
    }
  }

  private void reply(String routingKey, FeedPurchaseMessage message) {
    amqp.convertAndSend(SagaRoutes.EXCHANGE, routingKey, message);
  }
}
