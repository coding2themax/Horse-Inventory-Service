package com.coding2themax.horse.horseinventoryservice.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Keeps horse goods in memory. {@link com.coding2themax.horse.horseinventoryservice.controller.HorseGoodsController}
 * needs a {@link HorseGoodService} for the app to start; this fills that gap
 * until the goods are stored in a database.
 */
@Service
public class InMemoryHorseGoodService implements HorseGoodService {

  private final Map<String, HorseGoods> goods = new ConcurrentHashMap<>();

  @Override
  public Flux<HorseGoods> getAllHorseGoods() {
    return Flux.fromIterable(goods.values());
  }

  @Override
  public Mono<HorseGoods> getHorseGoodById(String id) {
    return Mono.justOrEmpty(goods.get(id));
  }

  @Override
  public Mono<HorseGoods> createHorseGood(HorseGoods horseGood) {
    if (horseGood.getGoodsId() == null || horseGood.getGoodsId().isBlank()) {
      horseGood.setGoodsId(UUID.randomUUID().toString());
    }
    goods.put(horseGood.getGoodsId(), horseGood);
    return Mono.just(horseGood);
  }

  @Override
  public Mono<HorseGoods> updateHorseGood(String id, HorseGoods horseGood) {
    return Mono.justOrEmpty(goods.computeIfPresent(id, (key, old) -> horseGood.withGoodsId(key)));
  }

  @Override
  public Mono<HorseGoods> deleteHorseGood(String id) {
    return Mono.justOrEmpty(goods.remove(id));
  }
}
