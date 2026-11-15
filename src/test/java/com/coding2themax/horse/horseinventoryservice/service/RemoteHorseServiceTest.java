package com.coding2themax.horse.horseinventoryservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

  private static org.openapitools.client.model.Horse remote(int id, String color, String pattern, String training) {
    return new org.openapitools.client.model.Horse()
        .id(id).name("Horse " + id).age(5).breed("Arabian")
        .color(color).coatPattern(pattern).trainingLevel(training);
  }

  @Test
  void toDomainMapsAllFields() {
    Horse horse = RemoteHorseService.toDomain(remote(7, "bay", "roan", "advanced"));

    assertEquals("7", horse.getId());
    assertEquals("Horse 7", horse.getName());
    assertEquals(5, horse.getAge());
    assertEquals("Arabian", horse.getBreed());
    assertNull(horse.getColor(), "unknown colour maps to null");
    assertEquals(HorseCoatPattern.ROAN, horse.getCoatPattern());
    assertEquals(HorseTraining.ADVANCED, horse.getTraining());
  }

  @Test
  void toDomainHandlesNulls() {
    Horse horse = RemoteHorseService.toDomain(new org.openapitools.client.model.Horse().coatPattern(" "));

    assertNull(horse.getId());
    assertEquals(0, horse.getAge());
    assertNull(horse.getCoatPattern());
    assertNull(horse.getTraining());
  }

  @Test
  void toClientMapsAllFields() {
    Horse horse = new Horse.Builder().withName("Willow").withAge(4).withBreed("Mustang")
        .withColor(HorseColor.DUN).withCoatPattern(HorseCoatPattern.PINTO)
        .withTraining(HorseTraining.BASIC).build();
    horse.setId("3");

    org.openapitools.client.model.Horse client = RemoteHorseService.toClient(horse);

    assertEquals(3, client.getId());
    assertEquals("Willow", client.getName());
    assertEquals("DUN", client.getColor());
    assertEquals("PINTO", client.getCoatPattern());
    assertEquals("BASIC", client.getTrainingLevel());
  }

  @Test
  void toClientHandlesNulls() {
    org.openapitools.client.model.Horse client = RemoteHorseService.toClient(new Horse.Builder().build());

    assertNull(client.getId());
    assertNull(client.getColor());
    assertNull(client.getCoatPattern());
    assertNull(client.getTrainingLevel());
  }

  @Test
  void createGetAndDeleteMapResults() {
    when(repository.createHorse(any())).thenReturn(Mono.just(remote(1, "BLACK", null, null)));
    when(repository.getHorseById("1")).thenReturn(Mono.just(remote(1, "BLACK", null, null)));
    when(repository.deleteHorse("1")).thenReturn(Mono.just(remote(1, "BLACK", null, null)));
    when(repository.getHorseById("2")).thenReturn(Mono.empty());

    StepVerifier.create(service.createHorse(new Horse.Builder().withName("Horse 1").build()))
        .assertNext(h -> assertEquals("1", h.getId())).verifyComplete();
    StepVerifier.create(service.getHorse("1"))
        .assertNext(h -> assertEquals(HorseColor.BLACK, h.getColor())).verifyComplete();
    StepVerifier.create(service.deleteHorse("1")).expectNextCount(1).verifyComplete();
    StepVerifier.create(service.getHorse("2")).verifyComplete();
  }

  @Test
  void findHorsesFiltersOnNonNullCriteria() {
    when(repository.getAllHorses()).thenReturn(Flux.just(
        remote(1, "BLACK", "PINTO", "BASIC"),
        remote(2, "BLACK", "ROAN", "ADVANCED"),
        remote(3, "WHITE", "PINTO", "BASIC")));

    StepVerifier.create(service.findHorses(null, null, null)).expectNextCount(3).verifyComplete();
    StepVerifier.create(service.findHorses(HorseColor.BLACK, null, null)).expectNextCount(2).verifyComplete();
    StepVerifier.create(service.findHorses(null, HorseCoatPattern.PINTO, HorseTraining.BASIC))
        .expectNextCount(2).verifyComplete();
    StepVerifier.create(service.findHorses(HorseColor.BLACK, HorseCoatPattern.ROAN, HorseTraining.ADVANCED))
        .assertNext(h -> assertEquals("2", h.getId())).verifyComplete();
    StepVerifier.create(service.findHorses(HorseColor.WHITE, null, HorseTraining.ADVANCED)).verifyComplete();
  }
}
