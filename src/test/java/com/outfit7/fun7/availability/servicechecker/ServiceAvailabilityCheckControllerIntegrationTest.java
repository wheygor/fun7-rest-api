package com.outfit7.fun7.availability.servicechecker;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.neovisionaries.i18n.CountryCode;
import com.outfit7.fun7.availability.servicechecker.controller.ServiceAvailabilityCheckController;
import com.outfit7.fun7.availability.servicechecker.model.ServiceAvailability;
import com.outfit7.fun7.availability.servicechecker.service.ServiceAvailabilityChecker;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@WebMvcTest(ServiceAvailabilityCheckController.class)
class ServiceAvailabilityCheckControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ServiceAvailabilityChecker serviceAvailabilityChecker;

  @Test
  void checkServicesAvailability_returns200Status() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    ZoneId timeZone = ZoneId.of("America/New_York");
    CountryCode countryCode = CountryCode.US;

    ServiceAvailability expectedServiceAvailability = new ServiceAvailability(true, true, true);

    doReturn(expectedServiceAvailability).when(serviceAvailabilityChecker).checkServices(timeZone, userId, countryCode);

    // when
    mockMvc.perform(get("/service-availability-check/")
            .param("timezone", timeZone.getId())
            .param("userId", userId.toString())
            .param("cc", countryCode.name())
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.multiplayerEnabled").value(true))
        .andExpect(jsonPath("$.customerSupportEnabled").value(true))
        .andExpect(jsonPath("$.adsEnabled").value(true))
        .andDo(MockMvcResultHandlers.print());

    // then
    verify(serviceAvailabilityChecker).checkServices(timeZone, userId, countryCode);
  }

  @Test
  void checkServicesAvailability_returns404StatusWhenInvalidCountryCode() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    ZoneId timeZone = ZoneId.of("America/New_York");
    String countryCode = "INVALID_CODE";

    // when
    mockMvc.perform(get("/service-availability-check/")
            .param("timezone", timeZone.getId())
            .param("userId", userId.toString())
            .param("cc", countryCode)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    // then
    verify(serviceAvailabilityChecker, never()).checkServices(any(), any(), any());
  }

  @Test
  void checkServicesAvailability_returns404StatusWhenInvalidUserId() throws Exception {
    // given
    String userId = "invalid_uuid";
    ZoneId timeZone = ZoneId.of("America/New_York");
    String countryCode = "INVALID_CODE";

    // when
    mockMvc.perform(get("/service-availability-check/")
            .param("timezone", timeZone.getId())
            .param("userId", userId)
            .param("cc", countryCode)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    // then
    verify(serviceAvailabilityChecker, never()).checkServices(any(), any(), any());
  }

  @Test
  void checkServicesAvailability_returns404StatusMissingTimeZoneParameter() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    CountryCode countryCode = CountryCode.US;

    // when
    mockMvc.perform(get("/service-availability-check/")
            .param("userId", userId.toString())
            .param("cc", countryCode.toString())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    // then
    verify(serviceAvailabilityChecker, never()).checkServices(any(), any(), any());
  }

  @Test
  void checkServicesAvailability_returns404StatusMissingUserIdParameter() throws Exception {
    // given
    ZoneId timeZone = ZoneId.of("America/New_York");
    CountryCode countryCode = CountryCode.US;

    // when
    mockMvc.perform(get("/service-availability-check/")
            .param("timezone", timeZone.getId())
            .param("cc", countryCode.toString())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    // then
    verify(serviceAvailabilityChecker, never()).checkServices(any(), any(), any());
  }

  @Test
  void checkServicesAvailability_returns404StatusMissingCountryCodeParameter() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    ZoneId timeZone = ZoneId.of("America/New_York");

    // when
    mockMvc.perform(get("/service-availability-check/")
            .param("userId", userId.toString())
            .param("timezone", timeZone.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    // then
    verify(serviceAvailabilityChecker, never()).checkServices(any(), any(), any());
  }

  @Test
  void checkServicesAvailability_lowerCaseCountryCodeIsAccepted() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    ZoneId timeZone = ZoneId.of("America/New_York");
    CountryCode countryCode = CountryCode.SI;

    // when
    mockMvc.perform(get("/service-availability-check/")
            .param("timezone", timeZone.getId())
            .param("userId", userId.toString())
            .param("cc", countryCode.name().toLowerCase())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // then
    verify(serviceAvailabilityChecker).checkServices(timeZone, userId, countryCode);
  }
}