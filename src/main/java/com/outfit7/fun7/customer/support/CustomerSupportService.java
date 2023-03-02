package com.outfit7.fun7.customer.support;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * A service class that provides functionality to check whether the customer support service is currently available based on the current time and the business
 * hours of the support team.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerSupportService {

  private final static LocalTime CUSTOMER_SUPPORT_START_TIME = LocalTime.of(9, 0);
  private final static LocalTime CUSTOMER_SUPPORT_END_TIME = LocalTime.of(15, 0);
  private final static ZoneId CUSTOMER_SUPPORT_LJUBLJANA_ZONE_ID = ZoneId.of("Europe/Ljubljana");

  private final Clock clock;

  /**
   * Checks whether the customer support service is currently available based on the current time and the business hours of the support team.
   *
   * @return {@code true} if the customer support service is currently available, {@code false} otherwise
   */
  public boolean isCustomerSupportEnabled() {
    ZonedDateTime now = Instant.now(clock).atZone(CUSTOMER_SUPPORT_LJUBLJANA_ZONE_ID);

    boolean isBusinessDay = isBusinessDay(now);
    boolean isBusinessHour = isBusinessHour(now);

    boolean isCustomerServiceEnabled = (isBusinessDay && isBusinessHour);

    log.debug("Customer service is {}available at '{}'", (isCustomerServiceEnabled ? "" : "not "), now);

    return isCustomerServiceEnabled;
  }

  /**
   * Determines whether a given time falls on a business day (Monday - Friday)
   *
   * @param temporal the time to check
   * @return {@code true} if the time falls on a business day, {@code false} otherwise
   */
  private boolean isBusinessDay(Temporal temporal) {
    DayOfWeek dayOfWeek = DayOfWeek.from(temporal);

    return (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY);
  }

  /**
   * Determines whether a given time falls within the business hours of the support team
   *
   * @param temporal the time to check
   * @return {@code true} if the time falls within business hours, {@code false} otherwise
   */
  private boolean isBusinessHour(Temporal temporal) {
    LocalTime time = LocalTime.from(temporal);

    return time.isAfter(CUSTOMER_SUPPORT_START_TIME) &&
        time.isBefore(CUSTOMER_SUPPORT_END_TIME);
  }
}

