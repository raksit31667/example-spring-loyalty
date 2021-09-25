package com.raksit.example.loyalty.tracing;

import io.jaegertracing.internal.MDCScopeManager;
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogCorrelationConfiguration {

  @Bean
  public TracerBuilderCustomizer tracerBuilderCustomizer() {
    return builder -> {
      MDCScopeManager mdcScopeManager = new MDCScopeManager.Builder().build();
      builder.withScopeManager(mdcScopeManager);
    };
  }
}
