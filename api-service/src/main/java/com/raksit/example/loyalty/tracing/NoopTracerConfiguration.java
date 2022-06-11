package com.raksit.example.loyalty.tracing;

import io.opentracing.Tracer;
import io.opentracing.noop.NoopTracerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(value = "opentracing.jaeger.enabled", havingValue = "false")
@Configuration
public class NoopTracerConfiguration {

  @Bean
  public Tracer jaegerTracer() {
    return NoopTracerFactory.create();
  }
}
