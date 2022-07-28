package com.barterbay.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
      .exchangeStrategies(ExchangeStrategies.builder()
        .codecs(clientCodecConfigurer -> clientCodecConfigurer
          .defaultCodecs()
          .maxInMemorySize(8 * 1024 * 1024))
        .build())
      .build();
  }
}
