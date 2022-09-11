package com.raksit.example.loyalty.job.subscriptionimport.subscription.repository;

import com.raksit.example.loyalty.job.subscriptionimport.subscription.view.UserSubscriptionCount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserSubscriptionCountRepository extends CrudRepository<UserSubscriptionCount, UUID> {

  @Modifying
  @Query(value = "REFRESH MATERIALIZED VIEW user_subscription_count", nativeQuery = true)
  void refresh();
}
