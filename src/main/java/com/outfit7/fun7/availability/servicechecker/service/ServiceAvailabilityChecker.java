package com.outfit7.fun7.availability.servicechecker.service;

import com.neovisionaries.i18n.CountryCode;
import com.outfit7.fun7.ads.AdsService;
import com.outfit7.fun7.availability.servicechecker.model.ServiceAvailability;
import com.outfit7.fun7.customer.support.CustomerSupportService;
import com.outfit7.fun7.multiplayer.MultiplayerService;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for checking the availability of Fun7 services: ads, multiplayer, and customer support.
 */
@Service
@RequiredArgsConstructor
public class ServiceAvailabilityChecker {

  private final AdsService adsService;
  private final MultiplayerService multiplayerService;
  private final CustomerSupportService customerSupportService;

  /**
   * Checks the availability of Fun7 services: ads, multiplayer, and customer support.
   *
   * @param timeZone    the user's time zone
   * @param userId      the user's ID
   * @param countryCode the user's country code
   * @return a {@link ServiceAvailability} object indicating the availability of each service
   */
  public ServiceAvailability checkServices(final ZoneId timeZone, final UUID userId, final CountryCode countryCode) {

    CompletableFuture<Boolean> adsEnabledFuture = CompletableFuture.supplyAsync(
        () -> adsService.checkAdsAvailabilityForCountry(countryCode)
    );

    CompletableFuture<Boolean> multiplayerEnabledFuture = CompletableFuture.supplyAsync(
        () -> multiplayerService.canUserPlayMultiplayer(userId, timeZone, countryCode)
    );

    CompletableFuture<Boolean> customerSupportEnabledFuture = CompletableFuture.completedFuture(customerSupportService.isCustomerSupportEnabled());

    CompletableFuture.allOf(adsEnabledFuture, customerSupportEnabledFuture, multiplayerEnabledFuture).join();

    return new ServiceAvailability(multiplayerEnabledFuture.join(), customerSupportEnabledFuture.join(), adsEnabledFuture.join());
  }
}
