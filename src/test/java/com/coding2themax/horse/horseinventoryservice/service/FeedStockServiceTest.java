package com.coding2themax.horse.horseinventoryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseProduct;
import com.coding2themax.horse.horseinventoryservice.service.FeedStockService.Reservation;

class FeedStockServiceTest {

  private FeedStockService service;

  @BeforeEach
  void setUp() {
    service = new FeedStockService(List.of(
        new HorseProduct("hay", "Hay", FeedStockService.FEED_CATEGORY, 10, 18.5, 10.0),
        new HorseProduct("saddle", "Saddle", "TACK", 5, 300.0, 200.0)));
  }

  @Test
  void reserveTakesStockAndReturnsPrice() {
    Reservation reservation = service.reserve(UUID.randomUUID(), "hay", 4);

    assertThat(reservation.accepted()).isTrue();
    assertThat(reservation.unitPrice()).isEqualByComparingTo("18.50");
    assertThat(stockOf("hay")).isEqualTo(6);
  }

  @Test
  void reserveTwiceForSameSagaTakesStockOnce() {
    UUID sagaId = UUID.randomUUID();
    service.reserve(sagaId, "hay", 4);
    Reservation again = service.reserve(sagaId, "hay", 4);

    assertThat(again.accepted()).isTrue();
    assertThat(stockOf("hay")).isEqualTo(6);
  }

  @Test
  void reserveRejectsWhenNotEnoughStock() {
    Reservation reservation = service.reserve(UUID.randomUUID(), "hay", 11);

    assertThat(reservation.accepted()).isFalse();
    assertThat(reservation.rejectionReason()).isEqualTo("Only 10 of hay in stock");
    assertThat(stockOf("hay")).isEqualTo(10);
  }

  @Test
  void reserveRejectsUnknownFeedAndNonFeedProducts() {
    assertThat(service.reserve(UUID.randomUUID(), "oats", 1).rejectionReason()).isEqualTo("Unknown feed: oats");
    assertThat(service.reserve(UUID.randomUUID(), "saddle", 1).accepted()).isFalse();
  }

  @Test
  void reserveRejectsZeroQuantity() {
    assertThat(service.reserve(UUID.randomUUID(), "hay", 0).accepted()).isFalse();
  }

  @Test
  void releasePutsStockBackOnce() {
    UUID sagaId = UUID.randomUUID();
    service.reserve(sagaId, "hay", 4);

    service.release(sagaId);
    service.release(sagaId);

    assertThat(stockOf("hay")).isEqualTo(10);
  }

  @Test
  void releaseOfUnknownSagaDoesNothing() {
    service.release(UUID.randomUUID());

    assertThat(stockOf("hay")).isEqualTo(10);
  }

  @Test
  void reserveAfterReleaseIsRejected() {
    UUID sagaId = UUID.randomUUID();
    service.reserve(sagaId, "hay", 4);
    service.release(sagaId);

    assertThat(service.reserve(sagaId, "hay", 4).accepted()).isFalse();
    assertThat(stockOf("hay")).isEqualTo(10);
  }

  @Test
  void commitKeepsStockTakenAndCanRepeat() {
    UUID sagaId = UUID.randomUUID();
    service.reserve(sagaId, "hay", 4);

    service.commit(sagaId);
    service.commit(sagaId);

    assertThat(stockOf("hay")).isEqualTo(6);
  }

  @Test
  void commitWithoutReservationFails() {
    assertThatThrownBy(() -> service.commit(UUID.randomUUID())).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void releaseAfterCommitFails() {
    UUID sagaId = UUID.randomUUID();
    service.reserve(sagaId, "hay", 4);
    service.commit(sagaId);

    assertThatThrownBy(() -> service.release(sagaId)).isInstanceOf(IllegalStateException.class);
    assertThat(stockOf("hay")).isEqualTo(6);
  }

  @Test
  void defaultCatalogHasFeed() {
    assertThat(new FeedStockService().getFeedStock())
        .extracting(HorseProduct::getProductId)
        .contains("alfalfa-hay", "premium-pellets");
  }

  private int stockOf(String feedId) {
    return service.getFeedStock().stream()
        .filter(p -> p.getProductId().equals(feedId))
        .findFirst()
        .orElseThrow()
        .getStock();
  }
}
