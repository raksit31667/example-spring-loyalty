package com.raksit.example.loyalty.job.migratelegacy.legacy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(value = "legacy", url = "${legacy.url}", configuration = LegacyLoyaltyClientConfiguration.class)
public interface LegacyLoyaltyClient {

  @GetMapping(value = "/loyalty/users/{id}", produces = APPLICATION_JSON_VALUE)
  LegacyLoyaltyUser findUserById(@PathVariable String id);
}
