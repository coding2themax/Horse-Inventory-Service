// implement the HorseService interface
package com.coding2themax.horse.horseinventoryservice.service;

import java.util.List;

import com.coding2themax.horse.horseinventoryservice.model.horse.Horse;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseCoatPattern;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseColor;
import com.coding2themax.horse.horseinventoryservice.model.horse.HorseTraining;

/**
 * The HorseClient class implements the HorseService interface and provides
 * methods to interact with the horse inventory service.
 */
public class HorseClient implements HorseService {

  @Override
  public Horse createHorse(Horse horse) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createHorse'");
  }

  @Override
  public Horse getHorse(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getHorse'");
  }

  @Override
  public List<Horse> getHorses() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getHorses'");
  }

  @Override
  public Horse updateHorse(Horse horse) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateHorse'");
  }

  @Override
  public void deleteHorse(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteHorse'");
  }

  @Override
  public List<Horse> getHorsesByColor(HorseColor color) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getHorsesByColor'");
  }

  @Override
  public List<Horse> getHorsesByCoatPattern(HorseCoatPattern coatPattern) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getHorsesByCoatPattern'");
  }

  @Override
  public List<Horse> getHorsesByTraining(HorseTraining training) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getHorsesByTraining'");
  }

  @Override
  public List<Horse> getHorsesByColorAndCoatPattern(HorseColor color, HorseCoatPattern coatPattern) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getHorsesByColorAndCoatPattern'");
  }

  @Override
  public List<Horse> getHorsesByColorAndTraining(HorseColor color, HorseTraining training) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getHorsesByColorAndTraining'");
  }

  @Override
  public List<Horse> getHorsesByCoatPatternAndTraining(HorseCoatPattern coatPattern, HorseTraining training) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getHorsesByCoatPatternAndTraining'");
  }

  @Override
  public List<Horse> getHorsesByColorAndCoatPatternAndTraining(HorseColor color, HorseCoatPattern coatPattern,
      HorseTraining training) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getHorsesByColorAndCoatPatternAndTraining'");
  }

}
