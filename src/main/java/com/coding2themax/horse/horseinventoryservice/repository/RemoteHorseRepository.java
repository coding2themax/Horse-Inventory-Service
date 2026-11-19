package com.coding2themax.horse.horseinventoryservice.repository;

import org.openapitools.client.api.HorseReactiveRepoServiceApi;
import org.openapitools.client.model.Horse;
import org.openapitools.client.model.HorseCoatPattern;
import org.openapitools.client.model.HorseColor;
import org.openapitools.client.model.HorseTrainingLevel;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link HorseRepository} backed by the remote horse data service via the
 * generated OpenAPI client. A 404 from the remote service is mapped to an
 * empty result (or {@code false} for deletes).
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
  public Mono<Horse> updateHorse(Integer id, Horse horse) {
    return Mono.defer(() -> api.updateHorse(id, horse))
        .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty());
  }

  @Override
  public Mono<Boolean> deleteHorse(Integer id) {
    // The spec returns 204 with no body, so success is read from the status code.
    return Mono.defer(() -> api.deleteHorseWithHttpInfo(id))
        .map(response -> response.getStatusCode().is2xxSuccessful())
        .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.just(false));
  }

  @Override
  public Flux<Horse> getAllHorses(HorseColor color, HorseCoatPattern coatPattern,
      HorseTrainingLevel trainingLevel) {
    return Flux.defer(() -> api.getAllHorses(color, coatPattern, trainingLevel));
  }

  @Override
  public Mono<Horse> getHorseById(Integer id) {
    return Mono.defer(() -> api.getHorseById(id))
        .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty());
  }
}
