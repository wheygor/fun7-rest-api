package com.outfit7.fun7.availability.servicechecker.controller;

import com.neovisionaries.i18n.CountryCode;
import com.outfit7.fun7.availability.servicechecker.model.ServiceAvailability;
import com.outfit7.fun7.availability.servicechecker.service.ServiceAvailabilityChecker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.time.ZoneId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-availability-check")
public class ServiceAvailabilityCheckController {

  private final ServiceAvailabilityChecker serviceAvailabilityChecker;

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Check Fun7 services availability",
      parameters = {
          @Parameter(name = "timezone", description = "Time zone of the user", required = true, examples = {
              @ExampleObject(name = "New York", value = "America/New_York"),
              @ExampleObject(name = "US Pacific", value = "US/Pacific"),
              @ExampleObject(name = "Ljubljana", value = "Europe/Ljubljana"),
          }),
          @Parameter(name = "userId", description = "User ID in UUID format", example = "5e6d919a-9de9-42d3-a167-7dd0ca9b2805", required = true),
          @Parameter(name = "cc", description = "Country code of the user", required = true, examples = {
              @ExampleObject(name = "Slovenia", value = "SI"),
              @ExampleObject(name = "United States of America", value = "US")
          })
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "Success",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ServiceAvailability.class))),
          @ApiResponse(responseCode = "400", description = "Bad request",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
          @ApiResponse(responseCode = "500", description = "Internal Server Error",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
      },
      description = "Check availability of Fun7 services: ads, multiplayer, and customer support.")
  public ResponseEntity<ServiceAvailability> checkServicesAvailability(
      @RequestParam(value = "timezone") final ZoneId timeZone,
      @RequestParam(value = "userId") final UUID userId,
      @RequestParam(value = "cc") final CountryCode countryCode) {
    ServiceAvailability serviceAvailability = serviceAvailabilityChecker.checkServices(timeZone,
        userId, countryCode);
    return new ResponseEntity<>(serviceAvailability, HttpStatus.OK);
  }
}
