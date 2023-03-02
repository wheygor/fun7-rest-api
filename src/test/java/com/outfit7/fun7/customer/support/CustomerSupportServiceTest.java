package com.outfit7.fun7.customer.support;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.outfit7.fun7.MutableClock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class CustomerSupportServiceTest {

  private CustomerSupportService service;

  private MutableClock mutableClock;

  @BeforeAll
  public void init() {
    mutableClock = MutableClock.create();
    service = new CustomerSupportService(mutableClock);
  }

  @Test
  public void isCustomerSupportEnabled_isDisabledWhenTimeIsDuringNonBusinessHoursInLjubljana() {
    // given
    ZonedDateTime notDuringBusinessHours = ZonedDateTime.of(
        LocalDate.of(2023, 2, 28),
        LocalTime.of(22, 0),
        ZoneId.of("Europe/Ljubljana"));

    mutableClock.alterTime(notDuringBusinessHours);

    // when
    boolean isCustomerSupportAvailable = service.isCustomerSupportEnabled();

    // then
    assertFalse(isCustomerSupportAvailable);
  }

  @Test
  public void isCustomerSupportEnabled_isDisabledWhenTimeIsDuringTheWeekendInLjubljana() {
    // given
    ZonedDateTime duringTheWeekend = ZonedDateTime.of(
        LocalDate.of(2023, 2, 25),
        LocalTime.of(12, 0),
        ZoneId.of("Europe/Ljubljana"));

    mutableClock.alterTime(duringTheWeekend);

    // when
    boolean result = service.isCustomerSupportEnabled();

    // then
    assertFalse(result);
  }

  @Test
  public void isCustomerSupportEnabled_isEnabledWhenTimeIsDuringTheWeekAndBusinessHoursInLjubljana() {
    // given
    ZonedDateTime duringTheWeekAndBusinessHours = ZonedDateTime.of(
        LocalDate.of(2023, 2, 28),
        LocalTime.of(12, 0),
        ZoneId.of("Europe/Ljubljana"));

    mutableClock.alterTime(duringTheWeekAndBusinessHours);

    // when
    boolean result = service.isCustomerSupportEnabled();

    // then
    assertTrue(result);
  }

  @Test
  public void isCustomerSupportEnabled_isDisabledWhenTimeIsDuringTheWeekAndBusinessHoursInNewYork() {
    // given
    ZonedDateTime duringTheWeekAndBusinessHours = ZonedDateTime.of(
        LocalDate.of(2023, 3, 1),
        LocalTime.of(15, 0),
        ZoneId.of("America/New_York"));

    mutableClock.alterTime(duringTheWeekAndBusinessHours);

    // when
    boolean result = service.isCustomerSupportEnabled();

    // then
    assertFalse(result);
  }
}