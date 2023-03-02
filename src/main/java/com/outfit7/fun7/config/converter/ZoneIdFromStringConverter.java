package com.outfit7.fun7.config.converter;

import java.time.ZoneId;
import org.springframework.core.convert.converter.Converter;

public class ZoneIdFromStringConverter implements Converter<String, ZoneId> {

  @Override
  public ZoneId convert(String source) {
    try {
      return ZoneId.of(source);
    } catch (Exception e) {
      throw new RuntimeException("woosh");
    }
  }
}
