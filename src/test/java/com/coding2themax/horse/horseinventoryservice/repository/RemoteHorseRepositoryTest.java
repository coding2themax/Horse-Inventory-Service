package com.coding2themax.horse.horseinventoryservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.client.api.HorseReactiveRepoServiceApi;
import org.openapitools.client.model.Horse;
import org.openapitools.client.model.HorseCoatPattern;
import org.openapitools.client.model.HorseColor;
import org.openapitools.client.model.HorseTrainingLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final Map<Integer, Supplier<Mono<ResponseEntity<Void>>>> deletes = new HashMap<>();
    private final List<Object> lastFilters = new ArrayList<>();
    private Flux<Horse> all = Flux.empty();

    @Override
    public Mono<Horse> createHorse(Horse horse) {
      return Mono.just(new Horse(1).name(horse.getName()));
    }

    @Override
    public Mono<Horse> updateHorse(Integer id, Horse horse) {
      return responses.getOrDefault("update:" + id, Mono::empty).get();
    }

    @Override
    public Mono<Horse> getHorseById(Integer id) {
      return responses.getOrDefault("get:" + id, Mono::empty).get();
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteHorseWithHttpInfo(Integer id) {
      return deletes.getOrDefault(id, () -> Mono.just(ResponseEntity.noContent().build())).get();
    }

    @Override
    public Flux<Horse> getAllHorses(HorseColor color, HorseCoatPattern coatPattern,
        HorseTrainingLevel trainingLevel) {
      lastFilters.clear();
      lastFilters.add(color);
      lastFilters.add(coatPattern);
      lastFilters.add(trainingLevel);
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
  void getAllForwardsFiltersToApi() {
    api.all = Flux.just(new Horse(1), new Horse(2));

    StepVerifier.create(repository.getAllHorses(HorseColor.BLACK, null, HorseTrainingLevel.BASIC))
        .expectNextCount(2).verifyComplete();
    assertEquals(List.of(HorseColor.BLACK, "none", HorseTrainingLevel.BASIC),
        api.lastFilters.stream().map(f -> f == null ? "none" : f).toList());
  }

  @Test
  void getByIdReturnsHorse() {
    Horse horse = new Horse(1);
    api.responses.put("get:1", () -> Mono.just(horse));

    StepVerifier.create(repository.getHorseById(1)).expectNext(horse).verifyComplete();
  }

  @Test
  void getByIdMapsNotFoundToEmpty() {
    api.responses.put("get:9", () -> Mono.error(error(HttpStatus.NOT_FOUND)));

    StepVerifier.create(repository.getHorseById(9)).verifyComplete();
  }

  @Test
  void getByIdPropagatesOtherErrors() {
    api.responses.put("get:1", () -> Mono.error(error(HttpStatus.INTERNAL_SERVER_ERROR)));

    StepVerifier.create(repository.getHorseById(1))
        .expectError(WebClientResponseException.InternalServerError.class)
        .verify();
  }

  @Test
  void updateReturnsUpdatedHorse() {
    Horse updated = new Horse(1).name("Renamed");
    api.responses.put("update:1", () -> Mono.just(updated));

    StepVerifier.create(repository.updateHorse(1, new Horse().name("Renamed")))
        .expectNext(updated).verifyComplete();
  }

  @Test
  void updateMapsNotFoundToEmpty() {
    api.responses.put("update:9", () -> Mono.error(error(HttpStatus.NOT_FOUND)));

    StepVerifier.create(repository.updateHorse(9, new Horse().name("x"))).verifyComplete();
  }

  @Test
  void deleteNoContentReturnsTrue() {
    StepVerifier.create(repository.deleteHorse(1)).expectNext(true).verifyComplete();
  }

  @Test
  void deleteMapsNotFoundToFalse() {
    api.deletes.put(9, () -> Mono.error(error(HttpStatus.NOT_FOUND)));

    StepVerifier.create(repository.deleteHorse(9)).expectNext(false).verifyComplete();
  }

  @Test
  void deletePropagatesOtherErrors() {
    api.deletes.put(1, () -> Mono.error(error(HttpStatus.INTERNAL_SERVER_ERROR)));

    StepVerifier.create(repository.deleteHorse(1))
        .expectError(WebClientResponseException.InternalServerError.class)
        .verify();
  }
}
