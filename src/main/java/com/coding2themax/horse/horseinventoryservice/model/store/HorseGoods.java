package com.coding2themax.horse.horseinventoryservice.model.store;

public class HorseGoods {
  private String goodsId;
  private String name;
  private String description;

  // Default constructor
  public HorseGoods() {
  }

  // Fluent constructor
  public HorseGoods(String goodsId, String name, String description) {
    this.goodsId = goodsId;
    this.name = name;
    this.description = description;
  }

  // Fluent builder
  public HorseGoods withGoodsId(String goodsId) {
    this.goodsId = goodsId;
    return this;
  }

  // Getters and Setters
  public String getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(String goodsId) {
    this.goodsId = goodsId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}