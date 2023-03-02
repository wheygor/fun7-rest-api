package com.outfit7.fun7.timezone;

import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

  @Service
  @RequiredArgsConstructor
  public class TimeZoneChecker {

    public static final List<String> AMERICAN_USA_ZONES = List.of(
        "America/Adak",
        "America/Anchorage",
        "America/Boise",
        "America/Chicago",
        "America/Denver",
        "America/Detroit",
        "America/Indiana/Indianapolis",
        "America/Indianapolis",
        "America/Indiana/Knox",
        "America/Indiana/Marengo",
        "America/Indiana/Petersburg",
        "America/Indiana/Tell_City",
        "America/Indiana/Vevay",
        "America/Indiana/Vincennes",
        "America/Indiana/Winamac",
        "America/Juneau",
        "America/Kentucky/Louisville",
        "America/Kentucky/Monticello",
        "America/Los_Angeles",
        "America/Menominee",
        "America/Metlakatla",
        "America/New_York",
        "America/Nome",
        "America/North_Dakota/Beulah",
        "America/North_Dakota/Center",
        "America/North_Dakota/New_Salem",
        "America/Phoenix",
        "America/Sitka",
        "America/Yakutat",
        "US/Alaska",
        "US/Arizona",
        "US/Central",
        "US/Eastern",
        "US/Hawaii",
        "US/Mountain",
        "US/Pacific",
        "EST",
        "PST",
        "CST",
        "-05:00",
        "-06:00",
        "-07:00"
    );

    public boolean isTimeZoneInsideUS(final ZoneId zoneId) {
      return AMERICAN_USA_ZONES.contains(zoneId.getId());
    }
  }
