package com.coding2themax.horse.horseinventoryservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;
import com.coding2themax.horse.horseinventoryservice.service.HorseGoodService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The HorseGoodsController class is responsible for handling HTTP requests
 * related to horse goods.
 */
@RestController
public class HorseGoodsController {

  private final HorseGoodService horseGoodService;

  public HorseGoodsController(HorseGoodService horseGoodService) {
    this.horseGoodService = horseGoodService;
  }

  @GetMapping("/horsegoods/{id}")
  public Mono<HorseGoods> getHorseGoodById(@PathVariable String id) {
    return horseGoodService.getHorseGoodById(id);
  }

  @GetMapping("/horsegoods")
  public Flux<HorseGoods> getAllHorseGoods() {
    return horseGoodService.getAllHorseGoods();
  }

  // add create horsegoods
  @PostMapping("/horsegoods")
  public Mono<HorseGoods> createHorseGood(@RequestBody HorseGoods horseGoods) {
    return horseGoodService.createHorseGood(horseGoods);
  }
  /// add update horsegoods
}
