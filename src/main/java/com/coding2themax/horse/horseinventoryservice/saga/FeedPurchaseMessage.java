package com.coding2themax.horse.horseinventoryservice.saga;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The message passed between saga steps over RabbitMQ.
 *
 * <p>This is a copy of the record in horse-store-order-service. Keep the field
 * names in step with that one.
 */
public record FeedPurchaseMessage(
    UUID sagaId,
    String customerId,
    String feedId,
    int quantity,
    String paymentToken,
    BigDecimal unitPrice,
    BigDecimal totalPrice,
    String paymentId,
    String failureReason) {

  public FeedPurchaseMessage withPricing(BigDecimal newUnitPrice) {
    BigDecimal total = newUnitPrice.multiply(BigDecimal.valueOf(quantity));
    return new FeedPurchaseMessage(sagaId, customerId, feedId, quantity, paymentToken,
        newUnitPrice, total, paymentId, failureReason);
  }

  public FeedPurchaseMessage withFailure(String reason) {
    return new FeedPurchaseMessage(sagaId, customerId, feedId, quantity, paymentToken,
        unitPrice, totalPrice, paymentId, reason);
  }
}
