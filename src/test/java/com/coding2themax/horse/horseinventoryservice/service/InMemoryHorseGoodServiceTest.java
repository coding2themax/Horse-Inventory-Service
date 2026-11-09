package com.coding2themax.horse.horseinventoryservice.service;

import org.junit.jupiter.api.Test;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;

import reactor.test.StepVerifier;

class InMemoryHorseGoodServiceTest {

  private final InMemoryHorseGoodService service = new InMemoryHorseGoodService();

  @Test
  void createAssignsIdAndCanBeRead() {
    HorseGoods created = service.createHorseGood(new HorseGoods(null, "Brush", "Soft brush")).block();

    StepVerifier.create(service.getHorseGoodById(created.getGoodsId()))
        .expectNextMatches(g -> g.getName().equals("Brush"))
        .verifyComplete();
    StepVerifier.create(service.getAllHorseGoods()).expectNextCount(1).verifyComplete();
  }

  @Test
  void updateReplacesOnlyExistingGoods() {
    service.createHorseGood(new HorseGoods("g1", "Brush", "Soft")).block();

    StepVerifier.create(service.updateHorseGood("g1", new HorseGoods(null, "Comb", "Metal")))
        .expectNextMatches(g -> g.getGoodsId().equals("g1") && g.getName().equals("Comb"))
        .verifyComplete();
    StepVerifier.create(service.updateHorseGood("missing", new HorseGoods())).verifyComplete();
  }

  @Test
  void deleteRemovesGoods() {
    service.createHorseGood(new HorseGoods("g1", "Brush", "Soft")).block();

    StepVerifier.create(service.deleteHorseGood("g1")).expectNextCount(1).verifyComplete();
    StepVerifier.create(service.getHorseGoodById("g1")).verifyComplete();
  }
}
