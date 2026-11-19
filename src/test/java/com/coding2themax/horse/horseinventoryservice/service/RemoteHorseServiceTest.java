package com.coding2themax.horse.horseinventoryservice.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.client.model.HorseTrainingLevel;

import com.coding2themax.horse.horseinventoryservice.model.horse.Horse;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseCoatPattern;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseColor;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseTraining;
import com.coding2themax.horse.horseinventoryservice.repository.HorseRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class RemoteHorseServiceTest {

  private HorseRepository repository;
  private RemoteHorseService service;

  @BeforeEach
  void setUp() {
    repository = mock(HorseRepository.class);
    service = new RemoteHorseService(repository);
  }

  private static org.openapitools.client.model.Horse remote(int id) {
    return new org.openapitools.client.model.Horse(id).name("Horse " + id).age(5).breed("Arabian");
  }

  @Test
  void toDomainMapsAllFields() {
    Horse horse = RemoteHorseService.toDomain(remote(7)
        .statusID(2)
        .color(org.openapitools.client.model.HorseColor.DUN)
        .coatPattern(org.openapitools.client.model.HorseCoatPattern.ROAN)
        .trainingLevel(HorseTrainingLevel.ADVANCED));

    assertEquals("7", horse.getId());
    assertEquals(2, horse.getStatusId());
    assertEquals("Horse 7", horse.getName());
    assertEquals(5, horse.getAge());
    assertEquals("Arabian", horse.getBreed());
    assertEquals(HorseColor.DUN, horse.getColor());
    assertEquals(HorseCoatPattern.ROAN, horse.getCoatPattern());
    assertEquals(HorseTraining.ADVANCED, horse.getTraining());
  }

  @Test
  void toDomainHandlesNulls() {
    Horse horse = RemoteHorseService.toDomain(new org.openapitools.client.model.Horse());

    assertNull(horse.getId());
    assertNull(horse.getStatusId());
    assertEquals(0, horse.getAge());
    assertNull(horse.getColor());
    assertNull(horse.getCoatPattern());
    assertNull(horse.getTraining());
  }

  @Test
  void toClientMapsAllFieldsExceptReadOnlyId() {
    Horse horse = new Horse.Builder().withName("Willow").withAge(4).withBreed("Mustang")
        .withColor(HorseColor.DUN).withCoatPattern(HorseCoatPattern.PINTO)
        .withTraining(HorseTraining.BASIC).build();
    horse.setId("3");
    horse.setStatusId(1);

    org.openapitools.client.model.Horse client = RemoteHorseService.toClient(horse);

    assertNull(client.getId(), "id is read-only in the spec");
    assertEquals(1, client.getStatusID());
    assertEquals("Willow", client.getName());
    assertEquals(org.openapitools.client.model.HorseColor.DUN, client.getColor());
    assertEquals(org.openapitools.client.model.HorseCoatPattern.PINTO, client.getCoatPattern());
    assertEquals(HorseTrainingLevel.BASIC, client.getTrainingLevel());
  }

  /** Guards against the domain enums and the spec's enums drifting apart. */
  @Test
  void everyEnumConstantRoundTrips() {
    roundTrip(HorseColor.values(), c -> RemoteHorseService.toClient(new Horse.Builder().withColor(c).build()),
        h -> h.getColor());
    roundTrip(HorseCoatPattern.values(),
        p -> RemoteHorseService.toClient(new Horse.Builder().withCoatPattern(p).build()), h -> h.getCoatPattern());
    roundTrip(HorseTraining.values(),
        t -> RemoteHorseService.toClient(new Horse.Builder().withTraining(t).build()), h -> h.getTraining());
    assertEquals(HorseColor.values().length, org.openapitools.client.model.HorseColor.values().length);
    assertEquals(HorseCoatPattern.values().length, org.openapitools.client.model.HorseCoatPattern.values().length);
    assertEquals(HorseTraining.values().length, HorseTrainingLevel.values().length);
  }

  private static <E> void roundTrip(E[] values, Function<E, org.openapitools.client.model.Horse> toClient,
      Function<Horse, E> getter) {
    for (E value : values) {
      assertEquals(value, getter.apply(RemoteHorseService.toDomain(toClient.apply(value))));
    }
  }

  @Test
  void createGetUpdateAndDeleteMapResults() {
    when(repository.createHorse(any())).thenReturn(Mono.just(remote(1)));
    when(repository.getHorseById(1)).thenReturn(Mono.just(remote(1)));
    when(repository.getHorseById(2)).thenReturn(Mono.empty());
    when(repository.updateHorse(eq(1), any())).thenReturn(Mono.just(remote(1).name("Renamed")));
    when(repository.updateHorse(eq(2), any())).thenReturn(Mono.empty());
    when(repository.deleteHorse(1)).thenReturn(Mono.just(true));
    when(repository.deleteHorse(2)).thenReturn(Mono.just(false));

    Horse input = new Horse.Builder().withName("Horse 1").build();
    StepVerifier.create(service.createHorse(input))
        .assertNext(h -> assertEquals("1", h.getId())).verifyComplete();
    StepVerifier.create(service.getHorse("1"))
        .assertNext(h -> assertEquals("Horse 1", h.getName())).verifyComplete();
    StepVerifier.create(service.getHorse("2")).verifyComplete();
    StepVerifier.create(service.updateHorse("1", input))
        .assertNext(h -> assertEquals("Renamed", h.getName())).verifyComplete();
    StepVerifier.create(service.updateHorse("2", input)).verifyComplete();
    StepVerifier.create(service.deleteHorse("1")).expectNext(true).verifyComplete();
    StepVerifier.create(service.deleteHorse("2")).expectNext(false).verifyComplete();
  }

  @ParameterizedTest
  @ValueSource(strings = { "abc", "3000000000", " " })
  void invalidIdsErrorAsSignalWithoutCallingRepository(String id) {
    Horse horse = new Horse.Builder().withName("Willow").build();

    StepVerifier.create(assertDoesNotThrow(() -> service.getHorse(id)))
        .expectError(IllegalArgumentException.class).verify();
    StepVerifier.create(assertDoesNotThrow(() -> service.updateHorse(id, horse)))
        .expectError(IllegalArgumentException.class).verify();
    StepVerifier.create(assertDoesNotThrow(() -> service.deleteHorse(id)))
        .expectError(IllegalArgumentException.class).verify();
    verifyNoInteractions(repository);
  }

  @Test
  void nullIdErrors() {
    StepVerifier.create(service.getHorse(null)).expectError(IllegalArgumentException.class).verify();
    verify(repository, never()).getHorseById(anyInt());
  }

  @Test
  void findHorsesPushesCriteriaToRepository() {
    when(repository.getAllHorses(org.openapitools.client.model.HorseColor.BLACK, null, HorseTrainingLevel.BASIC))
        .thenReturn(Flux.just(remote(1), remote(2)));

    StepVerifier.create(service.findHorses(HorseColor.BLACK, null, HorseTraining.BASIC))
        .expectNextCount(2).verifyComplete();
  }

  @Test
  void getHorsesRequestsUnfiltered() {
    when(repository.getAllHorses(isNull(), isNull(), isNull())).thenReturn(Flux.just(remote(1)));

    StepVerifier.create(service.getHorses())
        .assertNext(h -> assertEquals("1", h.getId())).verifyComplete();
  }
}
