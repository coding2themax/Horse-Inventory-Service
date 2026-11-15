package com.coding2themax.horse.horseinventoryservice.repository;

import org.openapitools.client.api.HorseReactiveRepoServiceApi;
import org.openapitools.client.model.Horse;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link HorseRepository} backed by the remote horse repository service via
 * the generated OpenAPI client. A 404 from the remote service is mapped to an
 * empty result.
 */
@Repository
public class RemoteHorseRepository implements HorseRepository {

  private final HorseReactiveRepoServiceApi api;

  public RemoteHorseRepository(HorseReactiveRepoServiceApi api) {
    this.api = api;
  }

  @Override
  public Mono<Horse> createHorse(Horse horse) {
    return Mono.defer(() -> api.createHorse(horse));
  }

  @Override
  public Mono<Horse> deleteHorse(String id) {
    return Mono.defer(() -> api.deleteHorse(id))
        .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty());
  }

  @Override
  public Flux<Horse> getAllHorses() {
    return Flux.defer(api::getAllHorses);
  }

  @Override
  public Mono<Horse> getHorseById(String id) {
    return Mono.defer(() -> api.getHorseById(id))
        .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty());
  }
}
