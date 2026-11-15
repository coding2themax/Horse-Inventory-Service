package com.coding2themax.horse.horseinventoryservice.service;

import java.util.Locale;
import java.util.Objects;

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
 * domain {@link Horse} and the remote service's client model. Unrecognised
 * enum values from the remote service are mapped to {@code null}.
 */
@Service
public class RemoteHorseService implements HorseService {

  private final HorseRepository horseRepository;

  public RemoteHorseService(HorseRepository horseRepository) {
    this.horseRepository = horseRepository;
  }

  @Override
  public Mono<Horse> createHorse(Horse horse) {
    return horseRepository.createHorse(toClient(horse)).map(RemoteHorseService::toDomain);
  }

  @Override
  public Mono<Horse> getHorse(String id) {
    return horseRepository.getHorseById(id).map(RemoteHorseService::toDomain);
  }

  @Override
  public Flux<Horse> getHorses() {
    return horseRepository.getAllHorses().map(RemoteHorseService::toDomain);
  }

  @Override
  public Mono<Horse> deleteHorse(String id) {
    return horseRepository.deleteHorse(id).map(RemoteHorseService::toDomain);
  }

  @Override
  public Flux<Horse> findHorses(HorseColor color, HorseCoatPattern coatPattern, HorseTraining training) {
    return getHorses().filter(horse -> (color == null || color == horse.getColor())
        && (coatPattern == null || coatPattern == horse.getCoatPattern())
        && (training == null || training == horse.getTraining()));
  }

  static Horse toDomain(org.openapitools.client.model.Horse source) {
    Horse horse = new Horse.Builder()
        .withName(source.getName())
        .withAge(Objects.requireNonNullElse(source.getAge(), 0))
        .withBreed(source.getBreed())
        .withTraining(parseEnum(HorseTraining.class, source.getTrainingLevel()))
        .withCoatPattern(parseEnum(HorseCoatPattern.class, source.getCoatPattern()))
        .withColor(parseEnum(HorseColor.class, source.getColor()))
        .build();
    horse.setId(source.getId() == null ? null : source.getId().toString());
    return horse;
  }

  static org.openapitools.client.model.Horse toClient(Horse source) {
    return new org.openapitools.client.model.Horse()
        .id(source.getId() == null ? null : Integer.valueOf(source.getId()))
        .name(source.getName())
        .age(source.getAge())
        .breed(source.getBreed())
        .trainingLevel(source.getTraining() == null ? null : source.getTraining().name())
        .coatPattern(source.getCoatPattern() == null ? null : source.getCoatPattern().name())
        .color(source.getColor() == null ? null : source.getColor().name());
  }

  private static <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    try {
      return Enum.valueOf(type, value.trim().toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
