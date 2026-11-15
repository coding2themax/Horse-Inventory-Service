package com.coding2themax.horse.horseinventoryservice.repository;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.client.api.HorseReactiveRepoServiceApi;
import org.openapitools.client.model.Horse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class RemoteHorseRepositoryTest {

  /**
   * Hand-written stub: Mockito cannot mock concrete classes on JDKs newer than
   * the bundled Byte Buddy supports, so responses are registered per call.
   */
  private static final class StubApi extends HorseReactiveRepoServiceApi {
    private final Map<String, Supplier<Mono<Horse>>> responses = new HashMap<>();
    private Flux<Horse> all = Flux.empty();

    @Override
    public Mono<Horse> createHorse(Horse horse) {
      return Mono.just(horse.id(1));
    }

    @Override
    public Mono<Horse> getHorseById(String id) {
      return responses.getOrDefault("get:" + id, Mono::empty).get();
    }

    @Override
    public Mono<Horse> deleteHorse(String id) {
      return responses.getOrDefault("delete:" + id, Mono::empty).get();
    }

    @Override
    public Flux<Horse> getAllHorses() {
      return all;
    }
  }

  private StubApi api;
  private RemoteHorseRepository repository;

  @BeforeEach
  void setUp() {
    api = new StubApi();
    repository = new RemoteHorseRepository(api);
  }

  private static WebClientResponseException error(HttpStatus status) {
    return WebClientResponseException.create(status.value(), status.getReasonPhrase(),
        HttpHeaders.EMPTY, new byte[0], StandardCharsets.UTF_8);
  }

  @Test
  void createDelegatesToApi() {
    StepVerifier.create(repository.createHorse(new Horse().name("Willow")))
        .expectNextMatches(h -> h.getId() == 1 && "Willow".equals(h.getName()))
        .verifyComplete();
  }

  @Test
  void getAllDelegatesToApi() {
    api.all = Flux.just(new Horse().id(1), new Horse().id(2));

    StepVerifier.create(repository.getAllHorses()).expectNextCount(2).verifyComplete();
  }

  @Test
  void getByIdReturnsHorse() {
    Horse horse = new Horse().id(1);
    api.responses.put("get:1", () -> Mono.just(horse));

    StepVerifier.create(repository.getHorseById("1")).expectNext(horse).verifyComplete();
  }

  @Test
  void getByIdMapsNotFoundToEmpty() {
    api.responses.put("get:9", () -> Mono.error(error(HttpStatus.NOT_FOUND)));

    StepVerifier.create(repository.getHorseById("9")).verifyComplete();
  }

  @Test
  void getByIdPropagatesOtherErrors() {
    api.responses.put("get:1", () -> Mono.error(error(HttpStatus.INTERNAL_SERVER_ERROR)));

    StepVerifier.create(repository.getHorseById("1"))
        .expectError(WebClientResponseException.InternalServerError.class)
        .verify();
  }

  @Test
  void deleteMapsNotFoundToEmpty() {
    api.responses.put("delete:9", () -> Mono.error(error(HttpStatus.NOT_FOUND)));

    StepVerifier.create(repository.deleteHorse("9")).verifyComplete();
  }

  @Test
  void deleteReturnsDeletedHorse() {
    Horse horse = new Horse().id(1);
    api.responses.put("delete:1", () -> Mono.just(horse));

    StepVerifier.create(repository.deleteHorse("1")).expectNext(horse).verifyComplete();
  }
}
