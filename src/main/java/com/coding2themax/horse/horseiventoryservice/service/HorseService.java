package com.coding2themax.horse.horseiventoryservice.service;

import java.util.List;

import com.coding2themax.horse.horseiventoryservice.model.Horse;
import com.coding2themax.horse.horseiventoryservice.model.HorseCoatPattern;
import com.coding2themax.horse.horseiventoryservice.model.HorseColor;
import com.coding2themax.horse.horseiventoryservice.model.HorseTraining;

public interface HorseService {
  Horse createHorse(Horse horse);

  Horse getHorse(Long id);

  List<Horse> getHorses();

  Horse updateHorse(Horse horse);

  void deleteHorse(Long id);

  List<Horse> getHorsesByColor(HorseColor color);

  List<Horse> getHorsesByCoatPattern(HorseCoatPattern coatPattern);

  List<Horse> getHorsesByTraining(HorseTraining training);

  List<Horse> getHorsesByColorAndCoatPattern(HorseColor color, HorseCoatPattern coatPattern);

  List<Horse> getHorsesByColorAndTraining(HorseColor color, HorseTraining training);

  List<Horse> getHorsesByCoatPatternAndTraining(HorseCoatPattern coatPattern, HorseTraining training);

  List<Horse> getHorsesByColorAndCoatPatternAndTraining(HorseColor color, HorseCoatPattern coatPattern,
      HorseTraining training);
}
