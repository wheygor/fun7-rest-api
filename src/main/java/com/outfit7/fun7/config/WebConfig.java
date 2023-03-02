package com.outfit7.fun7.config;

import com.outfit7.fun7.config.converter.CountryCodeFromStringConverter;
import com.outfit7.fun7.config.converter.ZoneIdFromStringConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new CountryCodeFromStringConverter());
    registry.addConverter(new ZoneIdFromStringConverter());
  }
}
