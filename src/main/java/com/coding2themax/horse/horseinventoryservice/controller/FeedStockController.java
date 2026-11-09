package com.coding2themax.horse.horseinventoryservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coding2themax.horse.horseinventoryservice.model.store.HorseProduct;
import com.coding2themax.horse.horseinventoryservice.service.FeedStockService;

/** Shows current feed stock. Stock changes only through the purchase saga. */
@RestController
public class FeedStockController {

  private final FeedStockService feedStock;

  public FeedStockController(FeedStockService feedStock) {
    this.feedStock = feedStock;
  }

  @GetMapping("/feed-stock")
  public List<HorseProduct> getFeedStock() {
    return feedStock.getFeedStock();
  }
}
