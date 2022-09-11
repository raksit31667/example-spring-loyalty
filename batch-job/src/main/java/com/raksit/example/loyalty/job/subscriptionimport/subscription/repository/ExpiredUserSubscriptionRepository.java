package com.raksit.example.loyalty.job.subscriptionimport.subscription.repository;

import com.raksit.example.loyalty.job.subscriptionimport.subscription.entity.ExpiredUserSubscription;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpiredUserSubscriptionRepository extends
    PagingAndSortingRepository<ExpiredUserSubscription, Long> {

}
