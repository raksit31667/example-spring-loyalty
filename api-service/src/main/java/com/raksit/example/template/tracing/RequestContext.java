package com.raksit.example.template.tracing;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RequestContext {
  public void put(String key, String value) {
    MDC.put(key, value);
  }

  public void setContextMap(Map<String, String> contextMap) {
    MDC.setContextMap(contextMap);
  }

  public void clear() {
    MDC.clear();
  }

  public Map<String, String> getCopyOfContextMap() {
    return MDC.getCopyOfContextMap();
  }
}
