package com.raksit.example.template.tracing;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.ContentCachingResponseWrapper;

@ExtendWith(MockitoExtension.class)
class RequestFilterTest {

  @Mock
  private RequestContext requestContext;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @Mock
  private ContentCachingResponseWrapper responseWrapper;

  @Spy
  @InjectMocks
  private RequestFilter requestFilter;

  @Test
  void shouldSetRequestContext_whenDoFilter_givenXRequestIdHeaderExists()
      throws ServletException, IOException {
    // Given
    String requestId = "some-request-id";
    when(request.getHeader("x-request-id")).thenReturn(requestId);

    // When
    requestFilter.doFilter(request, response, filterChain);

    // Then
    verify(requestContext).put("requestId", requestId);
    verify(filterChain).doFilter(eq(request), any());
  }

  @Test
  void shouldSetRequestHeader_whenDoFilter_givenXRequestIdHeaderExists()
      throws ServletException, IOException {
    // Given
    String requestId = "some-request-id";
    when(request.getHeader("x-request-id")).thenReturn(requestId);
    doReturn(responseWrapper).when(requestFilter).wrapResponse(response);

    // When
    requestFilter.doFilter(request, response, filterChain);

    // Then
    verify(responseWrapper).setHeader("requestId", requestId);
    verify(responseWrapper).copyBodyToResponse();
  }

  @Test
  void shouldRandomRequestContext_whenDoFilter_givenXRequestIdHeaderNotExist()
      throws ServletException, IOException {
    // Given
    when(request.getHeader("x-request-id")).thenReturn(null);

    // When
    requestFilter.doFilter(request, response, filterChain);

    // Then
    verify(requestContext).put(eq("requestId"), anyString());
    verify(filterChain).doFilter(eq(request), any());
  }

  @Test
  void shouldRandomRequestHeader_whenDoFilter_givenXRequestIdHeaderNotExist()
      throws ServletException, IOException {
    // Given
    when(request.getHeader("x-request-id")).thenReturn(null);
    doReturn(responseWrapper).when(requestFilter).wrapResponse(response);

    // When
    requestFilter.doFilter(request, response, filterChain);

    // Then
    verify(responseWrapper).setHeader(eq("requestId"), anyString());
    verify(responseWrapper).copyBodyToResponse();
  }
}