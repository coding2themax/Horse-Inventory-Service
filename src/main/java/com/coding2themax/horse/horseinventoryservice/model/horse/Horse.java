package com.coding2themax.horse.horseinventoryservice.model.horse;

public class Horse {

  private String name;
  private int age;
  private String breed;
  private HorseTraining training;
  private HorseCoatPattern coatPattern;

  public Horse(String name2, int age2, String breed2, HorseTraining training2, HorseCoatPattern coatPattern2,
      HorseColor color2) {
    // TODO Auto-generated constructor stub
  }

  public HorseCoatPattern getCoatPattern() {
    return coatPattern;
  }

  public void setCoatPattern(HorseCoatPattern coatPattern) {
    this.coatPattern = coatPattern;
  }

  public HorseColor getColor() {
    return color;
  }

  public void setColor(HorseColor color) {
    this.color = color;
  }

  private HorseColor color;

  public static class Builder {
    private String name;
    private int age;
    private String breed;
    private HorseTraining training;
    private HorseCoatPattern coatPattern;
    private HorseColor color;

    public Builder() {
    }

    public Builder withColor(HorseColor color) {
      this.color = color;
      return this;
    }

    public Builder withCoatPattern(HorseCoatPattern coatPattern) {
      this.coatPattern = coatPattern;
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withAge(int age) {
      this.age = age;
      return this;
    }

    public Builder withBreed(String breed) {
      this.breed = breed;
      return this;
    }

    public Builder withTraining(HorseTraining training) {
      this.training = training;
      return this;
    }

    public Horse build() {
      return new Horse(name, age, breed, training, coatPattern, color);
    }
  }

  public String getName() {
    return name;
  }

  public HorseTraining getTraining() {
    return training;
  }

  public void setTraining(HorseTraining training) {
    this.training = training;
  }

  public int getAge() {
    return age;
  }

  public String getBreed() {
    return breed;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public void setBreed(String breed) {
    this.breed = breed;
  }
}