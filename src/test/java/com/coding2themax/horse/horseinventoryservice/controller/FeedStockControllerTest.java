package com.coding2themax.horse.horseinventoryservice.controller;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseProduct;
import com.coding2themax.horse.horseinventoryservice.service.FeedStockService;

@WebFluxTest(FeedStockController.class)
class FeedStockControllerTest {

  @Autowired
  private WebTestClient client;

  @MockitoBean
  private FeedStockService feedStock;

  @Test
  void listsFeedStock() {
    when(feedStock.getFeedStock()).thenReturn(List.of(
        new HorseProduct("hay", "Hay", FeedStockService.FEED_CATEGORY, 7, 18.5, 10.0)));

    client.get().uri("/feed-stock")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$[0].productId").isEqualTo("hay")
        .jsonPath("$[0].stock").isEqualTo(7);
  }
}
