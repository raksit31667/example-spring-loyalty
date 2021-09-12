package com.raksit.example.loyalty.job;

import com.raksit.example.loyalty.legacy.LegacyLoyaltyClient;
import com.raksit.example.loyalty.legacy.LegacyLoyaltyUser;
import com.raksit.example.loyalty.user.User;
import org.springframework.batch.item.ItemProcessor;

public class LoyaltyUserItemProcessor implements ItemProcessor<User, LegacyLoyaltyUser> {

  private final LegacyLoyaltyClient legacyLoyaltyClient;

  public LoyaltyUserItemProcessor(LegacyLoyaltyClient legacyLoyaltyClient) {
    this.legacyLoyaltyClient = legacyLoyaltyClient;
  }

  @Override
  public LegacyLoyaltyUser process(User userFromDatabase) throws Exception {
    return legacyLoyaltyClient.findUserById(userFromDatabase.getId().toString());
  }
}