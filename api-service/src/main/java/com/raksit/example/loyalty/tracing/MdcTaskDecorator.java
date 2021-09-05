package com.raksit.example.loyalty.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MdcTaskDecorator implements TaskDecorator {

  @Autowired
  private RequestContext requestContext;

  @Override
  public Runnable decorate(Runnable runnable) {
    Map<String, String> contextMap = requestContext.getCopyOfContextMap();

    return () -> {
      try {
        Optional.ofNullable(contextMap).ifPresentOrElse(
            (Map<String, String> map) -> requestContext.setContextMap(map),
            () -> requestContext.clear()
        );
        runnable.run();
      } finally {
        requestContext.clear();
      }
    };
  }
}
