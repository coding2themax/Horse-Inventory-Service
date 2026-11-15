package com.coding2themax.horse.horseinventoryservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;
import com.coding2themax.horse.horseinventoryservice.service.HorseGoodService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(HorseGoodsController.class)
class HorseGoodsControllerTest {

  @MockBean
  HorseGoodService horseGoodService;

  @Autowired
  private WebTestClient webClient;

  @Test
  void testCreateHorseGood() {
    HorseGoods saved = new HorseGoods("1", "Saddle", "Leather western saddle");
    when(horseGoodService.createHorseGood(any(HorseGoods.class))).thenReturn(Mono.just(saved));

    webClient.post().uri("/horsegoods")
        .bodyValue(new HorseGoods(null, "Saddle", "Leather western saddle"))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.goodsId").isEqualTo("1")
        .jsonPath("$.name").isEqualTo("Saddle");
  }

  @Test
  void testGetAllHorseGoods() {
    when(horseGoodService.getAllHorseGoods()).thenReturn(Flux.just(
        new HorseGoods("1", "Saddle", "Leather"),
        new HorseGoods("2", "Bridle", "Nylon")));

    webClient.get().uri("/horsegoods")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(HorseGoods.class).hasSize(2);
  }

  @Test
  void testGetHorseGoodById() {
    when(horseGoodService.getHorseGoodById("1"))
        .thenReturn(Mono.just(new HorseGoods("1", "Saddle", "Leather")));

    webClient.get().uri("/horsegoods/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.name").isEqualTo("Saddle");
  }

  @Test
  void testGetHorseGoodByIdNotFound() {
    when(horseGoodService.getHorseGoodById("missing")).thenReturn(Mono.empty());

    webClient.get().uri("/horsegoods/missing")
        .exchange()
        .expectStatus().isNotFound();
  }
}
