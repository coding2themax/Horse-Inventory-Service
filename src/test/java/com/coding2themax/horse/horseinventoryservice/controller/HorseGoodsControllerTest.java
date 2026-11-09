package com.coding2themax.horse.horseinventoryservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;
import com.coding2themax.horse.horseinventoryservice.service.HorseGoodService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(HorseGoodsController.class)
public class HorseGoodsControllerTest {

  @MockitoBean
  HorseGoodService horseGoodService;

  @Autowired
  private WebTestClient webClient;

  @Test
  void testCreateHorseGood() {
    HorseGoods horseGoods = new HorseGoods("1", "Brush", "Soft brush");
    when(horseGoodService.createHorseGood(any())).thenReturn(Mono.just(horseGoods));

    webClient.post().uri("/horsegoods")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(horseGoods)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.goodsId").isEqualTo("1")
        .jsonPath("$.name").isEqualTo("Brush");
  }

  @Test
  void testGetAllHorseGoods() {
    when(horseGoodService.getAllHorseGoods()).thenReturn(Flux.just(
        new HorseGoods("1", "Brush", "Soft"), new HorseGoods("2", "Comb", "Metal")));

    webClient.get().uri("/horsegoods")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(HorseGoods.class).hasSize(2);
  }

  @Test
  void testGetHorseGoodById() {
    when(horseGoodService.getHorseGoodById("1")).thenReturn(Mono.just(new HorseGoods("1", "Brush", "Soft")));

    webClient.get().uri("/horsegoods/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.description").isEqualTo("Soft");
  }
}
