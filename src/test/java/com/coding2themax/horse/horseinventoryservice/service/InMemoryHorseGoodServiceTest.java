package com.coding2themax.horse.horseinventoryservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;

import reactor.test.StepVerifier;

class InMemoryHorseGoodServiceTest {

  private InMemoryHorseGoodService service;

  @BeforeEach
  void setUp() {
    service = new InMemoryHorseGoodService();
  }

  @Test
  void createAssignsIdWhenMissing() {
    StepVerifier.create(service.createHorseGood(new HorseGoods(null, "Saddle", "Leather")))
        .assertNext(saved -> {
          assertFalse(saved.getGoodsId().isBlank());
          assertEquals("Saddle", saved.getName());
        })
        .verifyComplete();
  }

  @Test
  void createKeepsProvidedIdAndCanBeFetched() {
    service.createHorseGood(new HorseGoods("abc", "Bridle", "Nylon")).block();

    StepVerifier.create(service.getHorseGoodById("abc"))
        .assertNext(found -> assertEquals("Bridle", found.getName()))
        .verifyComplete();
    StepVerifier.create(service.getAllHorseGoods()).expectNextCount(1).verifyComplete();
  }

  @Test
  void getMissingIsEmpty() {
    StepVerifier.create(service.getHorseGoodById("nope")).verifyComplete();
  }

  @Test
  void updateReplacesExistingAndIgnoresMissing() {
    service.createHorseGood(new HorseGoods("abc", "Bridle", "Nylon")).block();

    StepVerifier.create(service.updateHorseGood("abc", new HorseGoods(null, "Bridle", "Leather")))
        .assertNext(updated -> {
          assertEquals("abc", updated.getGoodsId());
          assertEquals("Leather", updated.getDescription());
        })
        .verifyComplete();
    StepVerifier.create(service.updateHorseGood("nope", new HorseGoods())).verifyComplete();
  }

  @Test
  void deleteRemovesItem() {
    service.createHorseGood(new HorseGoods("abc", "Bridle", "Nylon")).block();

    StepVerifier.create(service.deleteHorseGood("abc")).expectNextCount(1).verifyComplete();
    StepVerifier.create(service.getHorseGoodById("abc")).verifyComplete();
  }
}
