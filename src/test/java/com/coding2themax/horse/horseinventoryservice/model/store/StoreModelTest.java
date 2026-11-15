package com.coding2themax.horse.horseinventoryservice.model.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class StoreModelTest {

  @Test
  void horseGoodsAccessors() {
    HorseGoods goods = new HorseGoods().withGoodsId("g1");
    goods.setName("Halter");
    goods.setDescription("Rope halter");

    assertEquals("g1", goods.getGoodsId());
    assertEquals("Halter", goods.getName());
    assertEquals("Rope halter", goods.getDescription());

    goods.setGoodsId("g2");
    assertEquals("g2", goods.getGoodsId());
  }

  @Test
  void horseFeedAccessors() {
    LocalDate expiry = LocalDate.of(2027, 1, 1);
    HorseFeed feed = new HorseFeed("Oats", "Grain", expiry, 25.0);
    assertEquals("Oats", feed.getName());
    assertEquals("Grain", feed.getType());
    assertEquals(expiry, feed.getExpirationDate());
    assertEquals(25.0, feed.getWeight());

    HorseFeed empty = new HorseFeed();
    empty.setName("Hay");
    empty.setType("Forage");
    empty.setExpirationDate(expiry);
    empty.setWeight(40.0);
    assertEquals("Hay", empty.getName());
    assertEquals("Forage", empty.getType());
    assertEquals(expiry, empty.getExpirationDate());
    assertEquals(40.0, empty.getWeight());
  }

  @Test
  void horseProductAccessorsAndToString() {
    HorseProduct product = new HorseProduct("p1", "Brush", "Grooming", 10, 12.5, 6.0);
    assertEquals("p1", product.getProductId());
    assertEquals("Brush", product.getName());
    assertEquals("Grooming", product.getCategory());
    assertEquals(10, product.getStock());
    assertEquals(12.5, product.getPrice());
    assertEquals(6.0, product.getCost());
    assertTrue(product.toString().contains("productId='p1'"));

    HorseProduct empty = new HorseProduct();
    empty.setProductId("p2");
    empty.setName("Comb");
    empty.setCategory("Grooming");
    empty.setStock(3);
    empty.setPrice(4.0);
    assertEquals("p2", empty.getProductId());
    assertEquals("Comb", empty.getName());
    assertEquals("Grooming", empty.getCategory());
    assertEquals(3, empty.getStock());
    assertEquals(4.0, empty.getPrice());
  }
}
