package com.coding2themax.horse.horseinventoryservice.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseGoods;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * In-memory implementation of {@link HorseGoodService}. Data is held in a
 * thread-safe map and is lost on restart.
 */
@Service
public class InMemoryHorseGoodService implements HorseGoodService {

  private final Map<String, HorseGoods> store = new ConcurrentHashMap<>();

  @Override
  public Flux<HorseGoods> getAllHorseGoods() {
    return Flux.defer(() -> Flux.fromIterable(store.values()));
  }

  @Override
  public Mono<HorseGoods> getHorseGoodById(String id) {
    return Mono.fromSupplier(() -> store.get(id));
  }

  @Override
  public Mono<HorseGoods> createHorseGood(HorseGoods horseGood) {
    return Mono.fromSupplier(() -> {
      String id = horseGood.getGoodsId();
      if (id == null || id.isBlank()) {
        id = UUID.randomUUID().toString();
      }
      HorseGoods saved = new HorseGoods(id, horseGood.getName(), horseGood.getDescription());
      store.put(id, saved);
      return saved;
    });
  }

  @Override
  public Mono<HorseGoods> updateHorseGood(String id, HorseGoods horseGood) {
    return Mono.fromSupplier(() -> store.computeIfPresent(id,
        (key, existing) -> new HorseGoods(key, horseGood.getName(), horseGood.getDescription())));
  }

  @Override
  public Mono<HorseGoods> deleteHorseGood(String id) {
    return Mono.fromSupplier(() -> store.remove(id));
  }
}
