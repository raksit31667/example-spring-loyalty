package com.raksit.example.loyalty.tracing;

import io.opentracing.Tracer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Component
public class TraceIdResponseInjector implements Filter {

  private final Tracer tracer;

  private static final String TRACE_ID_HEADER = "Trace-Id";

  public TraceIdResponseInjector(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    Optional.ofNullable(tracer.scopeManager().activeSpan())
        .ifPresent(activeSpan -> httpServletResponse.setHeader(TRACE_ID_HEADER, activeSpan.context().toTraceId()));

    chain.doFilter(request, response);
  }
}
