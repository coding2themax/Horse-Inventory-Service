package com.coding2themax.horse.horseinventoryservice.config;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.HorseReactiveRepoServiceApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires the generated OpenAPI client for the remote horse repository service.
 */
@Configuration
public class HorseRepoClientConfig {

  @Bean
  public HorseReactiveRepoServiceApi horseReactiveRepoServiceApi(
      @Value("${horse.repo-service.base-url}") String baseUrl) {
    ApiClient apiClient = new ApiClient().setBasePath(baseUrl);
    return new HorseReactiveRepoServiceApi(apiClient);
  }
}
