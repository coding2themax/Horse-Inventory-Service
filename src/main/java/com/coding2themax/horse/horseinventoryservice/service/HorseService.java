package com.coding2themax.horse.horseinventoryservice.service;

import com.coding2themax.horse.horseinventoryservice.model.horse.Horse;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseCoatPattern;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseColor;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseTraining;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HorseService {
  Mono<Horse> createHorse(Horse horse);

  /** Emits the horse, or completes empty if it does not exist. */
  Mono<Horse> getHorse(String id);

  Flux<Horse> getHorses();

  /** Emits the deleted horse, or completes empty if it did not exist. */
  Mono<Horse> deleteHorse(String id);

  /**
   * Finds horses matching every non-null criterion. Passing {@code null} for a
   * criterion matches any value; passing all {@code null}s returns every horse.
   */
  Flux<Horse> findHorses(HorseColor color, HorseCoatPattern coatPattern, HorseTraining training);
}
