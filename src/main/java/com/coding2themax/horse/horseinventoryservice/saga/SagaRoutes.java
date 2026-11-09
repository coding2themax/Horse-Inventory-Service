package com.coding2themax.horse.horseinventoryservice.saga;

/**
 * RabbitMQ names shared by the feed purchase saga. These match the names in
 * horse-store-order-service.
 */
public final class SagaRoutes {

  public static final String EXCHANGE = "feed.purchase.saga";
  public static final String DEAD_LETTER_EXCHANGE = "feed.purchase.saga.dlx";
  public static final String DEAD_LETTER_QUEUE = "feed-purchase.dead-letter";

  public static final String INVENTORY_COMMANDS_QUEUE = "feed-purchase.inventory.commands";

  // Commands this service handles
  public static final String INVENTORY_RESERVE = "inventory.reserve";
  public static final String INVENTORY_COMMIT = "inventory.commit";
  public static final String INVENTORY_RELEASE = "inventory.release";

  // Replies this service sends
  public static final String INVENTORY_RESERVED = "saga.inventory.reserved";
  public static final String INVENTORY_REJECTED = "saga.inventory.rejected";
  public static final String INVENTORY_COMMITTED = "saga.inventory.committed";
  public static final String INVENTORY_RELEASED = "saga.inventory.released";

  private SagaRoutes() {
  }
}
