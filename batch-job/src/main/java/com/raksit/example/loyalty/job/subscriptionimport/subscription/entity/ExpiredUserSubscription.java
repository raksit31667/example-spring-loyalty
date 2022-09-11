package com.raksit.example.loyalty.job.subscriptionimport.subscription.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ExpiredUserSubscription {

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "expired_user_subscription_id_seq")
  @SequenceGenerator(name = "expired_user_subscription_id_seq",
      sequenceName = "expired_user_subscription_id_seq", allocationSize = 1)
  private Long id;

  private UUID userId;

  private String productId;

  private String productName;

  private Instant subscribedOn;

  private Instant activeUntil;

  public ExpiredUserSubscription(UUID userId, String productId, String productName, Instant subscribedOn,
      Instant activeUntil) {
    this.userId = userId;
    this.productId = productId;
    this.productName = productName;
    this.subscribedOn = subscribedOn;
    this.activeUntil = activeUntil;
  }
}
