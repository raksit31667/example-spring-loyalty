package com.raksit.example.loyalty.mock;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raksit.example.loyalty.legacy.LegacyLoyaltyUser;
import feign.Request.HttpMethod;
import java.util.UUID;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpStatus;

public class LegacyLoyaltyMockServer {

  private final ClientAndServer clientAndServer;

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public LegacyLoyaltyMockServer() throws JsonProcessingException {
    clientAndServer = ClientAndServer.startClientAndServer(9999);
  }

  public void addHappyPathExpectations(final UUID userId) throws JsonProcessingException {
    clientAndServer.when(
        request()
            .withMethod(HttpMethod.GET.name())
            .withPath("/loyalty/users/" + userId)
    ).respond(
        response()
            .withStatusCode(HttpStatus.OK.value())
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(
                OBJECT_MAPPER.writeValueAsString(new LegacyLoyaltyUser(userId.toString(), 100L)))
    );
  }

  public void addUnhappyPathExpectations(final UUID userId) {
    clientAndServer.when(
        request()
            .withMethod(HttpMethod.GET.name())
            .withPath("/loyalty/users/" + userId)
    ).respond(
        response()
            .withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .withContentType(MediaType.APPLICATION_JSON)
    );
  }

  public void stop() {
    clientAndServer.stop();
  }
}
