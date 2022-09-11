package com.raksit.example.loyalty.job.subscriptionimport.subscription.repository;

import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.UserSubscription;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface UserSubscriptionRepository extends
    PagingAndSortingRepository<UserSubscription, Long> {

  @Modifying
  @Query("DELETE FROM UserSubscription where activeUntil = ?1")
  void deleteAllByActiveUntilNot(Instant activeUntil);

  List<UserSubscription> findAllByActiveUntilNot(Instant activeUntil);
}
