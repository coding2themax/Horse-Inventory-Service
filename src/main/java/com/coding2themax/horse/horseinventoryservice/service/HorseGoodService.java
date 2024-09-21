package com.coding2themax.horse.horseinventoryservice.service;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HorseGoodService {
  Flux<HorseGoods> getAllHorseGoods();

  Mono<HorseGoods> getHorseGoodById(String id);

  Mono<HorseGoods> createHorseGood(HorseGoods horseGood);

  Mono<HorseGoods> updateHorseGood(String id, HorseGoods horseGood);

  Mono<HorseGoods> deleteHorseGood(String id);
}
