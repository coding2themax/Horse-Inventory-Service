package com.coding2themax.horse.horseinventoryservice.repository;

import org.openapitools.client.model.Horse;
import org.openapitools.client.model.HorseCoatPattern;
import org.openapitools.client.model.HorseColor;
import org.openapitools.client.model.HorseTrainingLevel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HorseRepository {

  Mono<Horse> createHorse(Horse horse);

  /** Emits the updated horse, or completes empty if it does not exist. */
  Mono<Horse> updateHorse(Integer id, Horse horse);

  /** Emits {@code true} if the horse was deleted, {@code false} if it did not exist. */
  Mono<Boolean> deleteHorse(Integer id);

  /**
   * Emits every horse matching all non-null criteria; {@code null} matches any
   * value. Filtering happens on the remote service.
   */
  Flux<Horse> getAllHorses(HorseColor color, HorseCoatPattern coatPattern, HorseTrainingLevel trainingLevel);

  /** Emits the horse, or completes empty if it does not exist. */
  Mono<Horse> getHorseById(Integer id);
}
