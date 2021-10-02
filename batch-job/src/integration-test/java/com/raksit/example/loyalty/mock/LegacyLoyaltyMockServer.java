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

  public LegacyLoyaltyMockServer() {
    clientAndServer = ClientAndServer.startClientAndServer(9999);
  }

  public void addHappyPathExpectations(LegacyLoyaltyUser legacyLoyaltyUser) throws JsonProcessingException {
    clientAndServer.when(
        request()
            .withMethod(HttpMethod.GET.name())
            .withPath("/loyalty/users/" + legacyLoyaltyUser.getId())
    ).respond(
        response()
            .withStatusCode(HttpStatus.OK.value())
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(
                OBJECT_MAPPER.writeValueAsString(legacyLoyaltyUser))
    );
  }

  public void addUnhappyPathExpectations(final String userId) {
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
