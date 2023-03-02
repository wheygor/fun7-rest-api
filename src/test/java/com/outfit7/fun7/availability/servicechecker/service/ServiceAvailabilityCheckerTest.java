package com.outfit7.fun7.availability.servicechecker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.neovisionaries.i18n.CountryCode;
import com.outfit7.fun7.ads.AdsService;
import com.outfit7.fun7.availability.servicechecker.model.ServiceAvailability;
import com.outfit7.fun7.customer.support.CustomerSupportService;
import com.outfit7.fun7.multiplayer.MultiplayerService;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceAvailabilityCheckerTest {

  @InjectMocks
  private ServiceAvailabilityChecker checker;

  @Mock
  private AdsService adsService;

  @Mock
  private CustomerSupportService customerSupportService;

  @Mock
  private MultiplayerService multiplayerService;

  @Test
  public void checkServices_shouldCallAllServiceMethods() {
    // given
    CountryCode countryCode = CountryCode.SI;
    UUID userId = UUID.randomUUID();
    ZoneId timeZone = ZoneId.of("Europe/Ljubljana");

    // when
    checker.checkServices(timeZone, userId, countryCode);

    // then
    verify(adsService).checkAdsAvailabilityForCountry(countryCode);
    verify(customerSupportService).isCustomerSupportEnabled();
    verify(multiplayerService).canUserPlayMultiplayer(userId, timeZone, countryCode);
  }

  @Test
  public void checkServices_shouldExecuteTheAdsAndMultiplayerServiceMethodsAsynchronously() throws InterruptedException {
    // given
    CountryCode countryCode = CountryCode.SI;
    UUID userId = UUID.randomUUID();
    ZoneId timeZone = ZoneId.of("Europe/Ljubljana");

    CountDownLatch latch = new CountDownLatch(2);

    when(adsService.checkAdsAvailabilityForCountry(ArgumentMatchers.any())).thenAnswer(invocation -> {
      Thread.sleep((long) (Math.random() * 1000));
      latch.countDown();
      return null;
    });

    when(multiplayerService.canUserPlayMultiplayer(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenAnswer(invocation -> {
      Thread.sleep((long) (Math.random() * 1000));
      latch.countDown();
      return null;
    });

    // when
    checker.checkServices(timeZone, userId, countryCode);

    boolean latchResult = latch.await(3, TimeUnit.SECONDS);

    //then
    assertEquals(0, latch.getCount());
    assertTrue(latchResult);
  }

  @Test
  void checkServices_AllEnabled() {
    // given
    ZoneId timeZone = ZoneId.of("Europe/Ljubljana");
    UUID userId = UUID.randomUUID();
    CountryCode countryCode = CountryCode.SI;

    when(adsService.checkAdsAvailabilityForCountry(countryCode)).thenReturn(false);
    when(multiplayerService.canUserPlayMultiplayer(userId, timeZone, countryCode)).thenReturn(true);
    when(customerSupportService.isCustomerSupportEnabled()).thenReturn(true);

    // when
    ServiceAvailability result = checker.checkServices(timeZone, userId, countryCode);

    // then
    assertFalse(result.adsEnabled());
    assertTrue(result.multiplayerEnabled());
    assertTrue(result.customerSupportEnabled());
  }
}