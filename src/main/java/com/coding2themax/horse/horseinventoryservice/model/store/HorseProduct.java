package com.coding2themax.horse.horseinventoryservice.model.store;

public class HorseProduct {
  private String productId;
  private String name;
  private String category;
  private int stock;
  private double price;
  private double cost;

  // Default constructor
  public HorseProduct() {
  }

  // Parameterized constructor
  public HorseProduct(String productId, String name, String category, int stock, double price, double cost) {
    this.productId = productId;
    this.name = name;
    this.category = category;
    this.stock = stock;
    this.price = price;
    this.cost = cost;
  }

  // Getters and Setters
  public double getCost() {
    return cost;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  // toString method
  @Override
  public String toString() {
    return "HorseProduct{" +
        "productId='" + productId + '\'' +
        ", name='" + name + '\'' +
        ", category='" + category + '\'' +
        ", price=" + price +
        ", stock=" + stock +
        '}';
  }
}