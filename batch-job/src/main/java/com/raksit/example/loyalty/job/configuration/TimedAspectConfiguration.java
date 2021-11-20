package com.raksit.example.loyalty.job.configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimedAspectConfiguration {

  @Bean
  public TimedAspect timedAspect(MeterRegistry meterRegistry) {
    return new TimedAspect(meterRegistry);
  }
}
