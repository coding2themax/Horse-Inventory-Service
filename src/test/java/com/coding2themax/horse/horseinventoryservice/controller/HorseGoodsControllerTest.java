package com.coding2themax.horse.horseinventoryservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;
import com.coding2themax.horse.horseinventoryservice.service.HorseGoodService;

@WebFluxTest(HorseGoodsController.class)
public class HorseGoodsControllerTest {

  @MockBean
  HorseGoodService horseGoodService;

  @Autowired
  private WebTestClient webClient;

  @Test
  void testCreateHorseGood() {

    HorseGoods horseGoods = new HorseGoods();
    // horseGoods.setId("1");

  }

  @Test
  void testGetAllHorseGoods() {

  }

  @Test
  void testGetHorseGoodById() {

  }
}
