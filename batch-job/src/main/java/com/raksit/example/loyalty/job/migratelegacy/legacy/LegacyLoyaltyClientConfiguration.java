package com.raksit.example.loyalty.job.migratelegacy.legacy;

import feign.Capability;
import feign.Contract;
import feign.Logger;
import feign.Logger.Level;
import feign.Request;
import feign.Response;
import feign.Retryer;
import feign.Retryer.Default;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;

import static feign.Util.valuesOrEmpty;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Slf4j
public class LegacyLoyaltyClientConfiguration {

  @Bean
  public Contract contract() {
    return new SpringMvcContract();
  }

  @Bean
  public Capability capability(MeterRegistry meterRegistry) {
    return new MicrometerCapability(meterRegistry);
  }

  @Bean
  public Retryer retryer() {
    return new Default(10, 60, 3);
  }

  @Bean
  public Level level() {
    return Level.BASIC;
  }

  @Bean
  public Logger logger() {
    return new Logger() {

      @Override
      protected void log(String configKey, String format, Object... args) {
        log.info("{}{}", methodTag(configKey), String.format(format, args));
      }

      @Override
      protected void logRequest(String configKey, Level logLevel, Request request) {
        log(configKey, "--> %s %s", request.httpMethod().name(), fromUriString(request.url()).build().getHost());
        request.headers().keySet().forEach(
            headerKey -> log(configKey, "%s: %s", headerKey, valuesOrEmpty(request.headers(), headerKey)));
      }

      @Override
      protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response,
          long elapsedTime) {
        log(configKey, "<-- %s %s %s (%s ms)", response.request().httpMethod().name(),
            fromUriString(response.request().url()).build().getHost(), response.status(), elapsedTime);

        return response;
      }
    };
  }
}
