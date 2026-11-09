package com.coding2themax.horse.horseinventoryservice.model.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class StoreModelTest {

  @Test
  void horseFeedHoldsValues() {
    LocalDate expires = LocalDate.of(2027, 1, 1);
    HorseFeed feed = new HorseFeed("Hay", "Forage", expires, 25.0);

    assertThat(feed.getName()).isEqualTo("Hay");
    assertThat(feed.getType()).isEqualTo("Forage");
    assertThat(feed.getExpirationDate()).isEqualTo(expires);
    assertThat(feed.getWeight()).isEqualTo(25.0);

    HorseFeed empty = new HorseFeed();
    empty.setName("Oats");
    empty.setType("Grain");
    empty.setExpirationDate(expires);
    empty.setWeight(10.0);
    assertThat(empty.getName()).isEqualTo("Oats");
    assertThat(empty.getType()).isEqualTo("Grain");
    assertThat(empty.getExpirationDate()).isEqualTo(expires);
    assertThat(empty.getWeight()).isEqualTo(10.0);
  }

  @Test
  void horseProductHoldsValues() {
    HorseProduct product = new HorseProduct("p1", "Hay", "FEED", 5, 18.5, 10.0);

    assertThat(product.getCost()).isEqualTo(10.0);
    assertThat(product.toString()).contains("p1", "Hay", "FEED", "18.5", "5");

    HorseProduct empty = new HorseProduct();
    empty.setProductId("p2");
    empty.setName("Oats");
    empty.setCategory("FEED");
    empty.setPrice(12.0);
    empty.setStock(3);
    assertThat(empty.getProductId()).isEqualTo("p2");
    assertThat(empty.getName()).isEqualTo("Oats");
    assertThat(empty.getCategory()).isEqualTo("FEED");
    assertThat(empty.getPrice()).isEqualTo(12.0);
    assertThat(empty.getStock()).isEqualTo(3);
  }

  @Test
  void horseGoodsHoldsValues() {
    HorseGoods goods = new HorseGoods();
    goods.setGoodsId("g1");
    goods.setName("Brush");
    goods.setDescription("Soft brush");

    assertThat(goods.getGoodsId()).isEqualTo("g1");
    assertThat(goods.getName()).isEqualTo("Brush");
    assertThat(goods.getDescription()).isEqualTo("Soft brush");
    assertThat(goods.withGoodsId("g2").getGoodsId()).isEqualTo("g2");
  }
}
