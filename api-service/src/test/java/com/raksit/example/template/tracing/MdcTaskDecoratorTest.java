package com.raksit.example.template.tracing;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MdcTaskDecoratorTest {

  @Mock
  private Runnable runnable;

  @Mock
  private RequestContext requestContext;

  @Mock
  private Map<String, String> contextMap;

  @InjectMocks
  private MdcTaskDecorator mdcTaskDecorator;

  @Test
  void shouldSetContextAndClearAndRun_whenDecorate_givenContextExists() {
    // Given
    when(requestContext.getCopyOfContextMap()).thenReturn(contextMap);

    // When
    mdcTaskDecorator.decorate(runnable).run();

    // Then
    verify(requestContext).setContextMap(contextMap);
    verify(requestContext).clear();
    verify(runnable).run();
  }

  @Test
  void shouldClearContextAndRun_whenDecorate_givenContextNotExist() {
    // Given
    when(requestContext.getCopyOfContextMap()).thenReturn(null);

    // When
    mdcTaskDecorator.decorate(runnable).run();

    // Then
    verify(requestContext, never()).setContextMap(contextMap);
    verify(requestContext, times(2)).clear();
    verify(runnable).run();
  }
}