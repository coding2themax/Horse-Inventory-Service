package com.coding2themax.horse.horseinventoryservice.repository;

import org.openapitools.client.model.Horse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HorseRepository {

  Mono<Horse> createHorse(Horse horse);

  Mono<Horse> deleteHorse(String id);

  Flux<Horse> getAllHorses();

  Mono<Horse> getHorseById(String id);
}
