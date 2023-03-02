package com.outfit7.fun7.config.converter;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.core.convert.converter.Converter;

public class CountryCodeFromStringConverter implements Converter<String, CountryCode> {

  @Override
  public CountryCode convert(String source) {
    return CountryCode.valueOf(source.toUpperCase());
  }
}
