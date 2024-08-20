package com.coding2themax.horse.horseinventoryservice.model.store;

import java.time.LocalDate;

public class HorseFeed {
  private String name;
  private String type;
  private LocalDate expirationDate;
  private double weight;

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  // Default constructor
  public HorseFeed() {
  }

  // Parameterized constructor
  public HorseFeed(String name, String type, LocalDate expirationDate, double weight) {
    this.name = name;
    this.type = type;
    this.expirationDate = expirationDate;
    this.weight = weight;
  }

  // Getters and Setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }

}