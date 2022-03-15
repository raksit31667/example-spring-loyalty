package com.raksit.example.loyalty.feature;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "feature")
public class DefaultFeatureFlagProperties {

  private Map<String, Boolean> flags;
}
