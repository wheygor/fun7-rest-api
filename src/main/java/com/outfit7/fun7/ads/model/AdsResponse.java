package com.outfit7.fun7.ads.model;

public record AdsResponse(String ads) {

  private final static String ADS_ENABLED_STRING_VALUE = "sure, why not!";

  public static AdsResponse empty() {
    return new AdsResponse("");
  }

  public boolean areAdsEnabled() {
    return ADS_ENABLED_STRING_VALUE.equals(this.ads);
  }
}
