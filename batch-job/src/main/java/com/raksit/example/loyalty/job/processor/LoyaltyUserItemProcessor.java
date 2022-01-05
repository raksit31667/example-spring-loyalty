package com.raksit.example.loyalty.job.processor;

import com.raksit.example.loyalty.legacy.LegacyLoyaltyClient;
import com.raksit.example.loyalty.legacy.LegacyLoyaltyUser;
import com.raksit.example.loyalty.legacy.LoyaltyTransaction;
import com.raksit.example.loyalty.user.entity.User;
import org.springframework.batch.item.ItemProcessor;

public class LoyaltyUserItemProcessor implements ItemProcessor<LoyaltyTransaction, User> {

  private final LegacyLoyaltyClient legacyLoyaltyClient;

  public LoyaltyUserItemProcessor(LegacyLoyaltyClient legacyLoyaltyClient) {
    this.legacyLoyaltyClient = legacyLoyaltyClient;
  }

  @Override
  public User process(LoyaltyTransaction loyaltyTransaction) {
    final LegacyLoyaltyUser legacyLoyaltyUser = legacyLoyaltyClient.findUserById(loyaltyTransaction.getMemberId());

    final User user = new User(legacyLoyaltyUser.getFirstName(), legacyLoyaltyUser.getLastName(),
        legacyLoyaltyUser.getEmail(), legacyLoyaltyUser.getPhone());
    user.setPoints(loyaltyTransaction.getPoints());
    user.setIsActive(legacyLoyaltyUser.getIsActive());
    return user;
  }
}
