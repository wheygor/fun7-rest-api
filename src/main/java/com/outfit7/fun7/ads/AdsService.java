package com.outfit7.fun7.ads;

import com.neovisionaries.i18n.CountryCode;
import com.outfit7.fun7.ads.client.AdsClient;
import com.outfit7.fun7.ads.model.AdsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * A service class that provides the functionality to check the availability of ads.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdsService {

  private final AdsClient client;

  /**
   * Checks if the ads feature is enabled for a specific country.
   *
   * @param countryCode the country code of the country to check for ads availability
   * @return {@code true} if the ads feature is enabled, {@code false} otherwise
   * @throws IllegalArgumentException if the {@code countryCode} parameter is {@code null}
   */
  public boolean checkAdsAvailabilityForCountry(final CountryCode countryCode) {
    if (countryCode == null) {
      throw new IllegalArgumentException("Country code must not be null!");
    }

    log.debug("Checking if ads are enabled for country: {}", countryCode.name());

    AdsResponse response = client.checkAdsAvailabilityFor(countryCode.name());

    boolean adsEnabled = response.areAdsEnabled();

    log.debug("Ads are {} for country: {}", adsEnabled ? "enabled" : "disabled", countryCode.getName());

    return adsEnabled;
  }
}
