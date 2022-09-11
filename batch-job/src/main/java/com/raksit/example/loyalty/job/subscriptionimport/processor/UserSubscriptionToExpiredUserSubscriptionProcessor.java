package com.raksit.example.loyalty.job.subscriptionimport.processor;

import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.ExpiredUserSubscription;
import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.UserSubscription;
import org.springframework.batch.item.ItemProcessor;

public class UserSubscriptionToExpiredUserSubscriptionProcessor
    implements ItemProcessor<UserSubscription, ExpiredUserSubscription> {

  @Override
  public ExpiredUserSubscription process(UserSubscription userSubscription) {
    return new ExpiredUserSubscription(
        userSubscription.getUserId(),
        userSubscription.getProductId(),
        userSubscription.getProductName(),
        userSubscription.getSubscribedOn(),
        userSubscription.getActiveUntil()
    );
  }
}
