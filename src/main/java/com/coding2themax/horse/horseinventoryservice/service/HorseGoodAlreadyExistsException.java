package com.coding2themax.horse.horseinventoryservice.service;

/** Signals that a horse good with the requested id already exists. */
public class HorseGoodAlreadyExistsException extends RuntimeException {

  private final String goodsId;

  public HorseGoodAlreadyExistsException(String goodsId) {
    super("Horse good already exists: " + goodsId);
    this.goodsId = goodsId;
  }

  public String getGoodsId() {
    return goodsId;
  }
}
