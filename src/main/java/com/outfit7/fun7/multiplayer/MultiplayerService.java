package com.outfit7.fun7.multiplayer;

import com.neovisionaries.i18n.CountryCode;
import com.outfit7.fun7.admin.service.UserManagementService;
import com.outfit7.fun7.admin.user.model.User;
import com.outfit7.fun7.timezone.TimeZoneChecker;
import java.time.ZoneId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * A service class that providers the functionality to check whether the multiplayer feature is enabled for a user.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class MultiplayerService {

  private static final int MIN_GAMES_FOR_MULTIPLAYER = 5;
  private static final CountryCode US_COUNTRY_CODE = CountryCode.US;

  private final TimeZoneChecker timeZoneChecker;
  private final UserManagementService userManagementService;

  /**
   * Checks if the multiplayer feature is enabled for the user with the specified ID, in the given time zone and country code.
   *
   * @param userId       the ID of the user to check
   * @param userTimeZone the time zone of the user
   * @param countryCode  the country code of the user
   * @return {@code true} if the multiplayer feature is enabled, {@code false} otherwise
   */
  public boolean canUserPlayMultiplayer(final UUID userId, final ZoneId userTimeZone, CountryCode countryCode) {
    log.debug("Checking multiplayer feature availability for user with ID '{}', country code '{}' in time zone '{}'",
        userId, countryCode, userTimeZone);

    if (!US_COUNTRY_CODE.equals(countryCode)) {
      log.debug("Multiplayer feature is disabled for user with ID '{}', country code '{}' in time zone '{}'",
          userId, countryCode, userTimeZone);
      return false;
    }

    if (!timeZoneChecker.isTimeZoneInsideUS(userTimeZone)) {
      log.debug("Multiplayer feature is disabled for user with ID '{}', country code '{}' in time zone '{}'",
          userId, countryCode, userTimeZone);
      return false;
    }

    final User user = userManagementService.getUserById(userId);

    boolean multiplayerEnabled = user.getPlayCount() >= MIN_GAMES_FOR_MULTIPLAYER;

    log.debug("Multiplayer feature is {} for user with ID '{}', country code '{}' in time zone '{}'",
        multiplayerEnabled ? "enabled" : "disabled",
        userId,
        countryCode,
        userTimeZone);

    return multiplayerEnabled;
  }
}
