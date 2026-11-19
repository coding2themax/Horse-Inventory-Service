package com.coding2themax.horse.horseinventoryservice.model.horse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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

  @Test
  void settersUpdateFields() {
    Horse horse = new Horse.Builder().build();
    horse.setId("1");
    horse.setName("Ash");
    horse.setAge(3);
    horse.setBreed("Morgan");
    horse.setTraining(HorseTraining.BASIC);
    horse.setCoatPattern(HorseCoatPattern.PINTO);
    horse.setColor(HorseColor.GRAY);

    assertEquals("1", horse.getId());
    assertEquals("Ash", horse.getName());
    assertEquals(3, horse.getAge());
    assertEquals("Morgan", horse.getBreed());
    assertEquals(HorseTraining.BASIC, horse.getTraining());
    assertEquals(HorseCoatPattern.PINTO, horse.getCoatPattern());
    assertEquals(HorseColor.GRAY, horse.getColor());
  }

  @ParameterizedTest
  @EnumSource(HorseTraining.class)
  void trainingFromStringParsesEveryLevel(HorseTraining level) {
    assertEquals(level, HorseTraining.fromString(level.name()));
  }

  @Test
  void trainingFromStringRejectsUnknown() {
    assertThrows(IllegalArgumentException.class, () -> HorseTraining.fromString("EXPERT"));
  }
}
