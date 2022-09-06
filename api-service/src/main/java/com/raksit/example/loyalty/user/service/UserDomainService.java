package com.raksit.example.loyalty.user.service;

import com.raksit.example.loyalty.user.entity.User;
import com.raksit.example.loyalty.user.entity.UserSubscriptionCount;
import com.raksit.example.loyalty.user.repository.UserRepository;
import com.raksit.example.loyalty.user.repository.UserSubscriptionCountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserDomainService {

  private final UserRepository userRepository;

  private final UserSubscriptionCountRepository userSubscriptionCountRepository;

  public UserDomainService(UserRepository userRepository,
      UserSubscriptionCountRepository userSubscriptionCountRepository) {
    this.userRepository = userRepository;
    this.userSubscriptionCountRepository = userSubscriptionCountRepository;
  }

  public Optional<User> findUserById(UUID userId) {

    return userRepository.findById(userId)
        .map(user -> {
          Long numberOfSubscriptions = userSubscriptionCountRepository
              .findByUserId(userId)
              .map(UserSubscriptionCount::getNumberOfSubscriptions)
              .orElse(0L);
          user.setId(userId);
          user.setNumberOfSubscriptions(numberOfSubscriptions);
          return user;
        });
  }
}
