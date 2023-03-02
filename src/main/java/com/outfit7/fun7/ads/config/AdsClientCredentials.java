package com.outfit7.fun7.ads.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "ads.client.credentials")
public class AdsClientCredentials {

  private final String username;

  private final String password;
}


