package com.coding2themax.horse.horseinventoryservice.model.horse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class HorseTest {

  @Test
  void builderSetsEveryField() {
    Horse horse = new Horse.Builder()
        .withName("Thunder")
        .withAge(7)
        .withBreed("Thoroughbred")
        .withTraining(HorseTraining.ADVANCED)
        .withCoatPattern(HorseCoatPattern.ROAN)
        .withColor(HorseColor.BLACK)
        .build();

    assertThat(horse.getName()).isEqualTo("Thunder");
    assertThat(horse.getAge()).isEqualTo(7);
    assertThat(horse.getBreed()).isEqualTo("Thoroughbred");
    assertThat(horse.getTraining()).isEqualTo(HorseTraining.ADVANCED);
    assertThat(horse.getCoatPattern()).isEqualTo(HorseCoatPattern.ROAN);
    assertThat(horse.getColor()).isEqualTo(HorseColor.BLACK);
  }

  @Test
  void settersChangeFields() {
    Horse horse = new Horse.Builder().build();
    horse.setName("Bella");
    horse.setAge(4);
    horse.setBreed("Quarter Horse");
    horse.setTraining(HorseTraining.BASIC);
    horse.setCoatPattern(HorseCoatPattern.PINTO);
    horse.setColor(HorseColor.BROWN);

    assertThat(horse.getName()).isEqualTo("Bella");
    assertThat(horse.getAge()).isEqualTo(4);
    assertThat(horse.getBreed()).isEqualTo("Quarter Horse");
    assertThat(horse.getTraining()).isEqualTo(HorseTraining.BASIC);
    assertThat(horse.getCoatPattern()).isEqualTo(HorseCoatPattern.PINTO);
    assertThat(horse.getColor()).isEqualTo(HorseColor.BROWN);
  }

  @ParameterizedTest
  @EnumSource(HorseTraining.class)
  void trainingParsesEveryName(HorseTraining training) {
    assertThat(HorseTraining.fromString(training.name())).isEqualTo(training);
  }

  @Test
  void trainingRejectsUnknownName() {
    assertThatThrownBy(() -> HorseTraining.fromString("EXPERT"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("EXPERT");
  }

  @ParameterizedTest
  @EnumSource(HorseColor.class)
  void colorRoundTrips(HorseColor color) {
    assertThat(HorseColor.valueOf(color.name())).isEqualTo(color);
  }

  @ParameterizedTest
  @EnumSource(HorseCoatPattern.class)
  void coatPatternRoundTrips(HorseCoatPattern pattern) {
    assertThat(HorseCoatPattern.valueOf(pattern.name())).isEqualTo(pattern);
  }
}
