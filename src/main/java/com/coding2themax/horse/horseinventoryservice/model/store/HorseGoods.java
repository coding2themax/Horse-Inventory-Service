package com.coding2themax.horse.horseinventoryservice.model.store;

public class HorseGoods {
  private String goodsId;
  private String horseProductId;

  public String getHorseProductId() {
    return horseProductId;
  }

  public void setHorseProductId(String horseProductId) {
    this.horseProductId = horseProductId;
  }

  private String name;
  private int quantity;
  private String location;
  private double price;
  private double cost;

  // Default constructor
  public HorseGoods() {
  }

  // Parameterized constructor
  public HorseGoods(String goodsId, String name, int quantity, String location, double price, double cost) {
    this.goodsId = goodsId;
    this.name = name;
    this.quantity = quantity;
    this.location = location;
    this.price = price;
    this.cost = cost;
  }

  // Getters and Setters
  public double getCost() {
    return cost;
  }

  public String getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(String goodsId) {
    this.goodsId = goodsId;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  // toString method
  @Override
  public String toString() {
    return "HorseGoods{" +
        "goodsId='" + goodsId + '\'' +
        ", name='" + name + '\'' +
        ", quantity=" + quantity +
        ", location='" + location + '\'' +
        '}';
  }
}