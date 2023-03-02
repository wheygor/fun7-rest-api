package com.outfit7.fun7.ads.client;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.outfit7.fun7.ads.config.AdsClientConfig;
import com.outfit7.fun7.ads.model.AdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "adsClient", url = "${ads.client.endpoint}",
    configuration = AdsClientConfig.class, fallback = AdsFallbackClient.class)
public interface AdsClient {

  @RequestMapping(method = GET, value = "/?countryCode={countryCode}", produces = APPLICATION_JSON_VALUE)
  AdsResponse checkAdsAvailabilityFor(@PathVariable("countryCode") String countryCode);
}
