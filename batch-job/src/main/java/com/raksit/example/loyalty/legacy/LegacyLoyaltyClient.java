package com.raksit.example.loyalty.legacy;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "legacy", url = "http://localhost:9080")
public interface LegacyLoyaltyClient {

  @GetMapping(value = "/loyalty/users/{id}", produces = APPLICATION_JSON_VALUE)
  LegacyLoyaltyUser findUserById(@PathVariable String id);
}
