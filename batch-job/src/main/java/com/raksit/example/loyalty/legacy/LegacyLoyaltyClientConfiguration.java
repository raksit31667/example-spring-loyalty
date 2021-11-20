package com.raksit.example.loyalty.legacy;

import feign.Capability;
import feign.Contract;
import feign.Retryer;
import feign.Retryer.Default;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;

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
}
