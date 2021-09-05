package com.raksit.example.loyalty.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestFilter implements Filter {

  private static final String REQUEST_ID_HEADER = "requestId";

  @Autowired
  private RequestContext requestContext;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String requestId = Optional.ofNullable(httpServletRequest.getHeader("x-request-id"))
        .orElseGet(() -> String.valueOf(UUID.randomUUID()));

    requestContext.put(REQUEST_ID_HEADER, requestId);
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    ContentCachingResponseWrapper responseWrapper = wrapResponse(httpServletResponse);
    chain.doFilter(request, responseWrapper);

    responseWrapper.setHeader(REQUEST_ID_HEADER, requestId);
    responseWrapper.copyBodyToResponse();
  }

  protected ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
    return new ContentCachingResponseWrapper(response);
  }
}
