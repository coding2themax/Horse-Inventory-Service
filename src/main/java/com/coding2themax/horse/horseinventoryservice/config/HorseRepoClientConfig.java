package com.coding2themax.horse.horseinventoryservice.config;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.HorseReactiveRepoServiceApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Wires the generated OpenAPI client for the remote horse repository service.
 * The client uses Spring Boot's auto-configured {@link WebClient.Builder} so it
 * picks up Boot-managed codecs ({@code spring.codec.*}), observation and
 * tracing.
 */
@Configuration
public class HorseRepoClientConfig {

  @Bean
  public HorseReactiveRepoServiceApi horseReactiveRepoServiceApi(
      WebClient.Builder webClientBuilder,
      @Value("${horse.repo-service.base-url}") String baseUrl) {
    ApiClient apiClient = new ApiClient(webClientBuilder.build()).setBasePath(baseUrl);
    return new HorseReactiveRepoServiceApi(apiClient);
  }
}
