package com.outfit7.fun7.ads;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.neovisionaries.i18n.CountryCode;
import com.outfit7.fun7.ads.client.AdsClient;
import com.outfit7.fun7.ads.model.AdsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdsServiceTest {

  @InjectMocks
  private AdsService adsService;

  @Mock
  private AdsClient adsClient;

  @Test
  public void areAdsEnabledFor_throwsExceptionAndNeverCallsAdsClientWhenCountryCodeArgumentIsNull() {
    // given
    // when
    // then
    assertThrows(IllegalArgumentException.class, () -> adsService.checkAdsAvailabilityForCountry(null));

    verify(adsClient, never()).checkAdsAvailabilityFor(ArgumentMatchers.anyString());
  }

  @Test
  public void areAdsEnabledFor_callsAdsClientWithCountryCodeSIWhenCountryCodeArgumentIsSI() {
    // given
    CountryCode adsCountryCodeParameter = CountryCode.SI;

    doReturn(AdsResponse.empty()).when(adsClient).checkAdsAvailabilityFor(adsCountryCodeParameter.name());

    // when
    adsService.checkAdsAvailabilityForCountry(adsCountryCodeParameter);

    // then
    verify(adsClient).checkAdsAvailabilityFor(adsCountryCodeParameter.name());
  }
}