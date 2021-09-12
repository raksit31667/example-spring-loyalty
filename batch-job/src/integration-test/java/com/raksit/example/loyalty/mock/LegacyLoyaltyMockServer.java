package com.raksit.example.loyalty.mock;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raksit.example.loyalty.legacy.LegacyLoyaltyUser;
import feign.Request.HttpMethod;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpStatus;

public class LegacyLoyaltyMockServer {

  private final ClientAndServer clientAndServer;

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public LegacyLoyaltyMockServer() throws JsonProcessingException {
    clientAndServer = ClientAndServer.startClientAndServer(9999);
    clientAndServer.when(
        request()
            .withMethod(HttpMethod.GET.name())
            .withPath("/loyalty/users/.*")
    ).respond(
        response()
            .withStatusCode(HttpStatus.OK.value())
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(OBJECT_MAPPER.writeValueAsString(new LegacyLoyaltyUser("", 100L)))
    );
  }

  public void stop() {
    clientAndServer.stop();
  }
}
