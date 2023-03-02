package com.outfit7.fun7;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.Temporal;

public class MutableClock extends Clock {

  private Instant instant;

  public MutableClock() {
    this.instant = Instant.now();
  }

  public static MutableClock create() {
    return new MutableClock();
  }

  @Override
  public ZoneId getZone() {
    throw new UnsupportedOperationException("This method is not implemented!");
  }

  @Override
  public Clock withZone(ZoneId zone) {
    throw new UnsupportedOperationException("This method is not implemented!");
  }

  @Override
  public Instant instant() {
    return instant;
  }

  public void alterTime(Temporal temporal) {
    this.instant = Instant.from(temporal);
  }
}
