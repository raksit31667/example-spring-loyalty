package com.raksit.example.loyalty.job.subscriptionimport.subscription.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Immutable
@Entity
public class UserSubscriptionCount {

  @Id
  private UUID userId;

  private Long numberOfSubscriptions;

  public UserSubscriptionCount(UUID userId, Long numberOfSubscriptions) {
    this.userId = userId;
    this.numberOfSubscriptions = numberOfSubscriptions;
  }
}
