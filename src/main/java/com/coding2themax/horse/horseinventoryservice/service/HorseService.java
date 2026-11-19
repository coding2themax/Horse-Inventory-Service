package com.coding2themax.horse.horseinventoryservice.service;

import com.coding2themax.horse.horseinventoryservice.model.horse.Horse;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseCoatPattern;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseColor;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseTraining;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Horse inventory operations. Methods that take an id error with
 * {@link IllegalArgumentException} if the id is not a valid integer.
 */
public interface HorseService {
  /** Creates a horse. The id is assigned by the data service; any id on the input is ignored. */
  Mono<Horse> createHorse(Horse horse);

  /** Emits the horse, or completes empty if it does not exist. */
  Mono<Horse> getHorse(String id);

  Flux<Horse> getHorses();

  /** Replaces the horse's fields. Emits the updated horse, or completes empty if it does not exist. */
  Mono<Horse> updateHorse(String id, Horse horse);

  /** Emits {@code true} if the horse was deleted, {@code false} if it did not exist. */
  Mono<Boolean> deleteHorse(String id);

  /**
   * Finds horses matching every non-null criterion. Passing {@code null} for a
   * criterion matches any value; passing all {@code null}s returns every horse.
   */
  Flux<Horse> findHorses(HorseColor color, HorseCoatPattern coatPattern, HorseTraining training);
}
