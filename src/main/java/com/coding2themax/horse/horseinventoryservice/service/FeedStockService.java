package com.coding2themax.horse.horseinventoryservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseProduct;

/**
 * Feed stock levels and the reservations made by feed purchase sagas.
 *
 * <p>Every method is keyed by saga id and safe to call twice, because RabbitMQ
 * can deliver the same command more than once.
 *
 * <p>Stock is held in memory and starts from {@link #defaultCatalog()}, so it
 * resets when the service restarts.
 */
@Service
public class FeedStockService {

  public static final String FEED_CATEGORY = "FEED";

  private enum State {
    RESERVED, COMMITTED, RELEASED
  }

  private record Hold(String feedId, int quantity, BigDecimal unitPrice, State state) {
    Hold withState(State newState) {
      return new Hold(feedId, quantity, unitPrice, newState);
    }
  }

  /**
   * The result of a reserve call.
   *
   * @param unitPrice the price per unit when accepted
   * @param rejectionReason why stock could not be reserved, or null when accepted
   */
  public record Reservation(BigDecimal unitPrice, String rejectionReason) {
    public boolean accepted() {
      return rejectionReason == null;
    }
  }

  private final Map<String, HorseProduct> products = new HashMap<>();
  private final Map<UUID, Hold> holds = new HashMap<>();

  public FeedStockService() {
    this(defaultCatalog());
  }

  public FeedStockService(List<HorseProduct> catalog) {
    catalog.forEach(product -> products.put(product.getProductId(), product));
  }

  /** Takes stock out of the available count and holds it for the saga. */
  public synchronized Reservation reserve(UUID sagaId, String feedId, int quantity) {
    Hold existing = holds.get(sagaId);
    if (existing != null) {
      if (existing.state() == State.RELEASED) {
        return new Reservation(null, "Reservation was already released");
      }
      return new Reservation(existing.unitPrice(), null);
    }
    HorseProduct product = products.get(feedId);
    if (product == null || !FEED_CATEGORY.equals(product.getCategory())) {
      return new Reservation(null, "Unknown feed: " + feedId);
    }
    if (quantity <= 0) {
      return new Reservation(null, "Quantity must be at least 1");
    }
    if (product.getStock() < quantity) {
      return new Reservation(null, "Only " + product.getStock() + " of " + feedId + " in stock");
    }
    product.setStock(product.getStock() - quantity);
    BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice()).setScale(2, RoundingMode.HALF_UP);
    holds.put(sagaId, new Hold(feedId, quantity, unitPrice, State.RESERVED));
    return new Reservation(unitPrice, null);
  }

  /** Makes a reservation final once payment has gone through. */
  public synchronized void commit(UUID sagaId) {
    Hold hold = holds.get(sagaId);
    if (hold == null || hold.state() == State.RELEASED) {
      throw new IllegalStateException("No reserved stock to commit for saga " + sagaId);
    }
    holds.put(sagaId, hold.withState(State.COMMITTED));
  }

  /** Puts reserved stock back. This is the saga's compensation step. */
  public synchronized void release(UUID sagaId) {
    Hold hold = holds.get(sagaId);
    if (hold == null || hold.state() == State.RELEASED) {
      return;
    }
    if (hold.state() == State.COMMITTED) {
      throw new IllegalStateException("Stock for saga " + sagaId + " is already committed");
    }
    HorseProduct product = products.get(hold.feedId());
    product.setStock(product.getStock() + hold.quantity());
    holds.put(sagaId, hold.withState(State.RELEASED));
  }

  /** A copy of the current feed stock, sorted by id. */
  public synchronized List<HorseProduct> getFeedStock() {
    List<HorseProduct> copy = new ArrayList<>();
    for (HorseProduct p : products.values()) {
      copy.add(new HorseProduct(p.getProductId(), p.getName(), p.getCategory(), p.getStock(), p.getPrice(),
          p.getCost()));
    }
    copy.sort(Comparator.comparing(HorseProduct::getProductId));
    return copy;
  }

  /** The feeds sold by the horse feed service, with starting stock. */
  static List<HorseProduct> defaultCatalog() {
    return List.of(
        new HorseProduct("premium-pellets", "Premium Horse Pellets", FEED_CATEGORY, 120, 24.99, 15.00),
        new HorseProduct("senior-feed", "Senior Horse Feed", FEED_CATEGORY, 80, 27.50, 17.25),
        new HorseProduct("performance-mix", "Performance Mix", FEED_CATEGORY, 60, 32.00, 20.10),
        new HorseProduct("maintenance-cubes", "Maintenance Cubes", FEED_CATEGORY, 150, 19.75, 11.40),
        new HorseProduct("alfalfa-hay", "Alfalfa Hay", FEED_CATEGORY, 200, 18.50, 10.00));
  }
}
