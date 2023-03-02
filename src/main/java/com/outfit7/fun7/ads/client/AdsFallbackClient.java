package com.outfit7.fun7.ads.client;

import com.outfit7.fun7.ads.model.AdsResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AdsFallbackClient implements AdsClient {

  @Override
  public AdsResponse checkAdsAvailabilityFor(String countryCode) {
    log.warn("Encountered an error during a request to Ads client for countryCode '{}'.",
        countryCode);
    return AdsResponse.empty();
  }
}
