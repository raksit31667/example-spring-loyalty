package com.raksit.example.loyalty.user.repository;

import com.raksit.example.loyalty.user.entity.UserSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserSubscriptionRepository extends CrudRepository<UserSubscription, UUID> {

}
