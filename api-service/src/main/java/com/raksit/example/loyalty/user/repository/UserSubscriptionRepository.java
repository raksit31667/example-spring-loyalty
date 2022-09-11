package com.raksit.example.loyalty.user.repository;

import com.raksit.example.loyalty.user.entity.UserSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionRepository extends CrudRepository<UserSubscription, Long> {

}
