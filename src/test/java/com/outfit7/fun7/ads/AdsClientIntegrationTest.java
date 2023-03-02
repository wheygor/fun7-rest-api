package com.outfit7.fun7.ads;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ActiveProfiles("integration-test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AdsWireMockServerConfig.class)
class AdsClientIntegrationTest {

  public static final String ADS_PARTNER_ENDPOINT = "/ads-partner-stub/";

  @Autowired
  private WireMockServer wireMockServer;

  @Autowired
  private AdsService adsService;

  @BeforeEach
  public void clear() {
    wireMockServer.resetMappings();
  }

  @Test
  public void checkAdsAvailabilityForCountry_returnsTrueWhenResponseIsSureWhyNot() {
    // given
    final CountryCode countryCode = CountryCode.SI;
    final String expectedResponse = "{ \"ads\": \"sure, why not!\" }";

    wireMockServer.stubFor(WireMock.get(urlPathEqualTo(ADS_PARTNER_ENDPOINT))
        .withQueryParam("countryCode", WireMock.equalTo(countryCode.name()))
        .willReturn(WireMock.aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", APPLICATION_JSON_VALUE)
            .withBody(expectedResponse)));

    // when
    boolean result = adsService.checkAdsAvailabilityForCountry(countryCode);

    // then
    assertTrue(result);
  }

  @Test
  public void checkAdsAvailabilityForCountry_returnsFalseWhenResponseIsYouShallNotPass() {
    // given
    final CountryCode countryCode = CountryCode.SI;
    final String expectedResponse = "{ \"ads\": \"you shall not pass!\" }";

    wireMockServer.stubFor(WireMock.get(urlPathEqualTo(ADS_PARTNER_ENDPOINT))
        .withQueryParam("countryCode", WireMock.equalTo(countryCode.name()))
        .willReturn(WireMock.aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", APPLICATION_JSON_VALUE)
            .withBody(expectedResponse)));

    // when
    boolean adsEnabled = adsService.checkAdsAvailabilityForCountry(countryCode);

    // then
    assertFalse(adsEnabled);
  }

  @Test
  public void checkAdsAvailabilityForCountry_returnsFalseWhenResponseIsInternalServerError() {
    // given
    final CountryCode countryCode = CountryCode.SI;

    wireMockServer.stubFor(WireMock.get(urlPathEqualTo(ADS_PARTNER_ENDPOINT))
        .withQueryParam("countryCode", WireMock.equalTo(countryCode.name()))
        .willReturn(WireMock.aResponse()
            .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    // when
    boolean adsEnabled = adsService.checkAdsAvailabilityForCountry(countryCode);

    // then
    assertFalse(adsEnabled);
  }

  @Test
  public void checkAdsAvailabilityForCountry_returnsFalseWhenResponseIsBadRequest() {
    // given
    final CountryCode countryCode = CountryCode.SI;

    wireMockServer.stubFor(WireMock.get(urlPathEqualTo(ADS_PARTNER_ENDPOINT))
        .withQueryParam("countryCode", WireMock.equalTo(countryCode.name()))
        .willReturn(WireMock.aResponse()
            .withStatus(HttpStatus.BAD_REQUEST.value())));
    // when
    boolean adsEnabled = adsService.checkAdsAvailabilityForCountry(countryCode);

    // then
    assertFalse(adsEnabled);
  }
}