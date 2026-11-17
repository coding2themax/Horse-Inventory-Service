package com.coding2themax.horse.horseinventoryservice.service;

import java.util.Objects;

import org.openapitools.client.model.HorseTrainingLevel;
import org.springframework.stereotype.Service;

import com.coding2themax.horse.horseinventoryservice.model.horse.Horse;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseCoatPattern;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseColor;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseTraining;
import com.coding2themax.horse.horseinventoryservice.repository.HorseRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@link HorseService} backed by {@link HorseRepository}. Converts between the
 * domain {@link Horse} and the remote service's client model. The domain and
 * client enums share constant names (enforced by the OpenAPI spec), so they are
 * mapped by name.
 */
@Service
public class RemoteHorseService implements HorseService {

  private final HorseRepository horseRepository;

  public RemoteHorseService(HorseRepository horseRepository) {
    this.horseRepository = horseRepository;
  }

  @Override
  public Mono<Horse> createHorse(Horse horse) {
    return Mono.fromCallable(() -> toClient(horse))
        .flatMap(horseRepository::createHorse)
        .map(RemoteHorseService::toDomain);
  }

  @Override
  public Mono<Horse> getHorse(String id) {
    return Mono.fromCallable(() -> parseId(id))
        .flatMap(horseRepository::getHorseById)
        .map(RemoteHorseService::toDomain);
  }

  @Override
  public Flux<Horse> getHorses() {
    return findHorses(null, null, null);
  }

  @Override
  public Mono<Horse> updateHorse(String id, Horse horse) {
    return Mono.fromCallable(() -> parseId(id))
        .flatMap(remoteId -> horseRepository.updateHorse(remoteId, toClient(horse)))
        .map(RemoteHorseService::toDomain);
  }

  @Override
  public Mono<Boolean> deleteHorse(String id) {
    return Mono.fromCallable(() -> parseId(id)).flatMap(horseRepository::deleteHorse);
  }

  @Override
  public Flux<Horse> findHorses(HorseColor color, HorseCoatPattern coatPattern, HorseTraining training) {
    return horseRepository.getAllHorses(
        mapEnum(color, org.openapitools.client.model.HorseColor.class),
        mapEnum(coatPattern, org.openapitools.client.model.HorseCoatPattern.class),
        mapEnum(training, HorseTrainingLevel.class))
        .map(RemoteHorseService::toDomain);
  }

  static Horse toDomain(org.openapitools.client.model.Horse source) {
    Horse horse = new Horse.Builder()
        .withName(source.getName())
        .withAge(Objects.requireNonNullElse(source.getAge(), 0))
        .withBreed(source.getBreed())
        .withTraining(mapEnum(source.getTrainingLevel(), HorseTraining.class))
        .withCoatPattern(mapEnum(source.getCoatPattern(), HorseCoatPattern.class))
        .withColor(mapEnum(source.getColor(), HorseColor.class))
        .build();
    horse.setId(source.getId() == null ? null : source.getId().toString());
    horse.setStatusId(source.getStatusID());
    return horse;
  }

  /** Maps to the client model. The id is read-only in the spec, so it is never sent. */
  static org.openapitools.client.model.Horse toClient(Horse source) {
    return new org.openapitools.client.model.Horse()
        .statusID(source.getStatusId())
        .name(source.getName())
        .age(source.getAge())
        .breed(source.getBreed())
        .trainingLevel(mapEnum(source.getTraining(), HorseTrainingLevel.class))
        .coatPattern(mapEnum(source.getCoatPattern(), org.openapitools.client.model.HorseCoatPattern.class))
        .color(mapEnum(source.getColor(), org.openapitools.client.model.HorseColor.class));
  }

  static Integer parseId(String id) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("Horse id is required");
    }
    try {
      return Integer.valueOf(id.trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Horse id must be an integer, got: " + id, e);
    }
  }

  private static <E extends Enum<E>> E mapEnum(Enum<?> value, Class<E> target) {
    return value == null ? null : Enum.valueOf(target, value.name());
  }
}
