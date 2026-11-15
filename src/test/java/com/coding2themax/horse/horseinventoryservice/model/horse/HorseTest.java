package com.coding2themax.horse.horseinventoryservice.model.horse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HorseTest {

  @Test
  void builderPopulatesAllFields() {
    Horse horse = new Horse.Builder()
        .withName("Willow")
        .withAge(7)
        .withBreed("Quarter Horse")
        .withTraining(HorseTraining.GREEN_BROKE)
        .withCoatPattern(HorseCoatPattern.ROAN)
        .withColor(HorseColor.BUCKSKIN)
        .build();

    assertEquals("Willow", horse.getName());
    assertEquals(7, horse.getAge());
    assertEquals("Quarter Horse", horse.getBreed());
    assertEquals(HorseTraining.GREEN_BROKE, horse.getTraining());
    assertEquals(HorseCoatPattern.ROAN, horse.getCoatPattern());
    assertEquals(HorseColor.BUCKSKIN, horse.getColor());
  }
}
