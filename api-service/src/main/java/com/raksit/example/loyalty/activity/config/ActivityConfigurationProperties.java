package com.raksit.example.loyalty.activity.config;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "activity")
public class ActivityConfigurationProperties {

  private Map<String, Long> pointMap;

  public Optional<Long> getPoints(String activityId) {
    return Optional.ofNullable(pointMap.get(activityId));
  }
}
