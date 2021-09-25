package com.raksit.example.loyalty.tracing;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.opentracing.ScopeManager;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraceIdResponseInjectorTest {

  @Mock
  private Tracer tracer;

  @Mock
  private ScopeManager scopeManager;

  @Mock
  private SpanContext spanContext;

  @Mock
  private Span activeSpan;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @InjectMocks
  private TraceIdResponseInjector traceIdResponseInjector;

  @Test
  void shouldSetTraceIdFromActiveSpanInResponseHeader_whenDoFilter_givenActiveSpanExists()
      throws ServletException, IOException {
    // Given
    when(tracer.scopeManager()).thenReturn(scopeManager);
    when(scopeManager.activeSpan()).thenReturn(activeSpan);
    when(activeSpan.context()).thenReturn(spanContext);
    when(spanContext.toTraceId()).thenReturn("abcd");

    // When
    traceIdResponseInjector.doFilter(request, response, filterChain);

    // Then
    verify(response).setHeader("Trace-Id", "abcd");
  }

  @Test
  void shouldContinueFilterChain_whenDoFilter_givenActiveSpanExists()
      throws ServletException, IOException {
    // Given
    when(tracer.scopeManager()).thenReturn(scopeManager);
    when(scopeManager.activeSpan()).thenReturn(activeSpan);
    when(activeSpan.context()).thenReturn(spanContext);
    when(spanContext.toTraceId()).thenReturn("abcd");

    // When
    traceIdResponseInjector.doFilter(request, response, filterChain);

    // Then
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldDoNothing_whenDoFilter_givenActiveSpanNotExist() throws ServletException, IOException {
    // Given
    when(tracer.scopeManager()).thenReturn(scopeManager);
    when(scopeManager.activeSpan()).thenReturn(null);

    // When
    traceIdResponseInjector.doFilter(request, response, filterChain);

    // Then
    verify(response, never()).setHeader(anyString(), anyString());
  }
}