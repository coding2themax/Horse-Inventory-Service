package com.coding2themax.horse.horseinventoryservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;
import com.coding2themax.horse.horseinventoryservice.service.HorseGoodService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(HorseGoodsController.class)
class HorseGoodsControllerTest {

  @MockitoBean
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

  @Test
  void testUpdateHorseGood() {
    when(horseGoodService.updateHorseGood(eq("1"), any(HorseGoods.class)))
        .thenReturn(Mono.just(new HorseGoods("1", "Saddle", "Synthetic")));

    webClient.put().uri("/horsegoods/1")
        .bodyValue(new HorseGoods(null, "Saddle", "Synthetic"))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.description").isEqualTo("Synthetic");
  }

  @Test
  void testUpdateHorseGoodNotFound() {
    when(horseGoodService.updateHorseGood(eq("missing"), any(HorseGoods.class))).thenReturn(Mono.empty());

    webClient.put().uri("/horsegoods/missing")
        .bodyValue(new HorseGoods(null, "Saddle", "Synthetic"))
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void testDeleteHorseGood() {
    when(horseGoodService.deleteHorseGood("1"))
        .thenReturn(Mono.just(new HorseGoods("1", "Saddle", "Leather")));

    webClient.delete().uri("/horsegoods/1")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void testDeleteHorseGoodNotFound() {
    when(horseGoodService.deleteHorseGood("missing")).thenReturn(Mono.empty());

    webClient.delete().uri("/horsegoods/missing")
        .exchange()
        .expectStatus().isNotFound();
  }
}
