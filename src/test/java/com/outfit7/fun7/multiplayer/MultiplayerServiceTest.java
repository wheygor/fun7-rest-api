package com.outfit7.fun7.multiplayer;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.neovisionaries.i18n.CountryCode;
import com.outfit7.fun7.admin.service.UserManagementService;
import com.outfit7.fun7.admin.user.model.User;
import com.outfit7.fun7.timezone.TimeZoneChecker;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MultiplayerServiceTest {

  @InjectMocks
  private MultiplayerService multiplayerService;

  @Mock
  private TimeZoneChecker timeZoneChecker;

  @Mock
  private UserManagementService userManagementService;

  @Test
  void isMultiplayerEnabled_isEnabledWhenInsideUSAndEnoughGamesPlayed() {
    UUID userId = UUID.fromString("192f614f-fd35-4c91-ad43-003fdaec345f");
    ZoneId userTimeZone = ZoneId.of("America/New_York");
    CountryCode countryCode = CountryCode.US;

    doReturn(true).when(timeZoneChecker).isTimeZoneInsideUS(userTimeZone);

    User user = User.create(userId, "rnd1234", 50);

    doReturn(user).when(userManagementService).getUserById(userId);

    boolean result = multiplayerService.canUserPlayMultiplayer(userId, userTimeZone, countryCode);

    assertTrue(result);

    verify(timeZoneChecker).isTimeZoneInsideUS(userTimeZone);
    verify(userManagementService).getUserById(userId);
  }

  @Test
  void isMultiplayerEnabled_isDisabledWhenInsideUSAndNotEnoughGamesPlayed() {
    UUID userId = UUID.fromString("192f614f-fd35-4c91-ad43-003fdaec345f");
    ZoneId userTimeZone = ZoneId.of("America/New_York");
    CountryCode countryCode = CountryCode.US;

    doReturn(true).when(timeZoneChecker).isTimeZoneInsideUS(userTimeZone);

    User user = User.create(userId, "rnd1234", 3);
    doReturn(user).when(userManagementService).getUserById(userId);

    boolean result = multiplayerService.canUserPlayMultiplayer(userId, userTimeZone, countryCode);

    assertFalse(result);

    verify(timeZoneChecker).isTimeZoneInsideUS(userTimeZone);
    verify(userManagementService).getUserById(userId);
  }

  @Test
  void isMultiplayerEnabled_isDisabledWhenNotInsideUSTimezone() {
    UUID userId = UUID.fromString("192f614f-fd35-4c91-ad43-003fdaec345f");
    ZoneId userTimeZone = ZoneId.of("Europe/London");
    CountryCode countryCode = CountryCode.US;

    doReturn(false).when(timeZoneChecker).isTimeZoneInsideUS(userTimeZone);

    boolean result = multiplayerService.canUserPlayMultiplayer(userId, userTimeZone, countryCode);

    assertFalse(result);

    verify(timeZoneChecker).isTimeZoneInsideUS(userTimeZone);
    verify(userManagementService, never()).getUserById(any(UUID.class));
  }

  @Test
  void isMultiplayerEnabled_isDisabledWhenInsideUSTimezoneButNotUSCountryCode() {
    UUID userId = UUID.fromString("192f614f-fd35-4c91-ad43-003fdaec345f");
    ZoneId userTimeZone = ZoneId.of("Europe/London");
    CountryCode countryCode = CountryCode.SI;

    boolean result = multiplayerService.canUserPlayMultiplayer(userId, userTimeZone, countryCode);

    assertFalse(result);

    verify(timeZoneChecker, never()).isTimeZoneInsideUS(userTimeZone);
    verify(userManagementService, never()).getUserById(any(UUID.class));
  }
}