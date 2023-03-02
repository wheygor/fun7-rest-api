package com.outfit7.fun7.ads;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AdsWireMockServerConfig {

  @Bean(initMethod = "start", destroyMethod = "stop")
  public WireMockServer mockServer() {
    return new WireMockServer(
        new WireMockConfiguration()
            .bindAddress("localhost")
            .port(35082)
    );
  }

}
