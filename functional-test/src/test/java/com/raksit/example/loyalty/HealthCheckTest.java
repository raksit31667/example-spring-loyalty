package com.raksit.example.loyalty;

import static com.raksit.example.loyalty.extension.ApiServiceExtension.dockerComposeContainer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.raksit.example.loyalty.extension.ApiServiceExtension;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@ExtendWith(ApiServiceExtension.class)
class HealthCheckTest {

  private final RestTemplate restTemplate = createRestTemplate();

  @Test
  void shouldReturnStatusUp_whenGetHealthCheck_givenAppIsRunning() {
    // When
    ResponseEntity<HealthCheckResponse> response = restTemplate.getForEntity(
        String.format("http://%s:%s/actuator/health",
            dockerComposeContainer.getServiceHost("app_1", 8080),
            dockerComposeContainer.getServicePort("app_1", 8080)),
        HealthCheckResponse.class);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(new HealthCheckResponse("UP"), response.getBody());
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private static class HealthCheckResponse {

    private String status;
  }

  private RestTemplate createRestTemplate() {
    final RestTemplate restTemplate = new RestTemplate();
    restTemplate.setMessageConverters(
        Collections.singletonList(new MappingJackson2HttpMessageConverter()));
    return restTemplate;
  }
}
