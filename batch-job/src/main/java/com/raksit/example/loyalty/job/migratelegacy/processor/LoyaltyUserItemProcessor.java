package com.raksit.example.loyalty.job.migratelegacy.processor;

import com.raksit.example.loyalty.job.migratelegacy.legacy.LegacyLoyaltyClient;
import com.raksit.example.loyalty.job.migratelegacy.legacy.LegacyLoyaltyUser;
import com.raksit.example.loyalty.job.migratelegacy.legacy.LoyaltyTransaction;
import com.raksit.example.loyalty.job.migratelegacy.user.entity.User;
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
    user.setActivityPoints(loyaltyTransaction.getPoints());
    user.setIsActive(legacyLoyaltyUser.getIsActive());
    return user;
  }
}
