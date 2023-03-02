package com.outfit7.fun7.timezone;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeZoneCheckerTest {

  private TimeZoneChecker checker;

  @BeforeEach
  void init() {
    checker = new TimeZoneChecker();
  }

  @Test
  void isInsideUS_returnsTrueWithAmericaZoneId() {
    ZoneId zoneId = ZoneId.of("America/New_York");

    boolean result = checker.isTimeZoneInsideUS(zoneId);

    assertTrue(result);
  }

  @Test
  void isInsideUS_returnsTrueWithUSZoneId() {
    ZoneId zoneId = ZoneId.of("US/Pacific");

    boolean result = checker.isTimeZoneInsideUS(zoneId);

    assertTrue(result);
  }

  @Test
  void isInsideUS_returnsTrueWithShortHandStandardZoneIds() {
    ZoneId EST = ZoneId.of("EST", ZoneId.SHORT_IDS);
    ZoneId CST = ZoneId.of("CST", ZoneId.SHORT_IDS);
    ZoneId PST = ZoneId.of("PST", ZoneId.SHORT_IDS);
    ZoneId MST = ZoneId.of("MST", ZoneId.SHORT_IDS);

    boolean result = checker.isTimeZoneInsideUS(EST);
    boolean result2 = checker.isTimeZoneInsideUS(CST);
    boolean result3 = checker.isTimeZoneInsideUS(PST);
    boolean result4 = checker.isTimeZoneInsideUS(MST);

    assertTrue(result);
    assertTrue(result2);
    assertTrue(result3);
    assertTrue(result4);
  }

  @Test
  void isInsideUS_returnsFalseWithNonUSZoneId() {
    ZoneId zoneEurope = ZoneId.of("Europe/London");
    ZoneId zoneAsia = ZoneId.of("Asia/Tokyo");

    boolean result = checker.isTimeZoneInsideUS(zoneEurope);
    boolean result2 = checker.isTimeZoneInsideUS(zoneAsia);

    assertFalse(result);
    assertFalse(result2);
  }
}