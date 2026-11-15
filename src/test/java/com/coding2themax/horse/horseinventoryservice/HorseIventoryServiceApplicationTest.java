package com.coding2themax.horse.horseinventoryservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.coding2themax.horse.horseinventoryservice.service.HorseGoodService;

@SpringBootTest
class HorseIventoryServiceApplicationTest {

  @Autowired
  private ApplicationContext context;

  @Test
  void contextLoadsWithHorseGoodService() {
    assertNotNull(context.getBean(HorseGoodService.class));
  }
}
