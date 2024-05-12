package com.coding2themax.horse.horseiventoryservice.model;

public enum HorseTraining {
  BASIC,
  INITIAL_BACKING,
  GREEN_BROKE,
  INTERMEDIATE,
  ADVANCED;

  public static HorseTraining fromString(String training) {
    switch (training) {
      case "BASIC":
        return BASIC;
      case "INITIAL_BACKING":
        return INITIAL_BACKING;
      case "GREEN_BROKE":
        return GREEN_BROKE;
      case "INTERMEDIATE":
        return INTERMEDIATE;
      case "ADVANCED":
        return ADVANCED;
      default:
        throw new IllegalArgumentException("Unknown training level: " + training);
    }
  }
} // Add this closing brace to complete the enum body.
