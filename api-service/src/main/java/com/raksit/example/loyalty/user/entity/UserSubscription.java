package com.raksit.example.loyalty.user.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Setter
@NoArgsConstructor
@Entity
public class UserSubscription {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private UUID userId;

  private String productId;

  private String productName;

  private Instant subscribedOn;

  private Instant activeUntil;

  public UserSubscription(UUID userId, String productId, String productName, Instant subscribedOn,
      Instant activeUntil) {
    this.userId = userId;
    this.productId = productId;
    this.productName = productName;
    this.subscribedOn = subscribedOn;
    this.activeUntil = activeUntil;
  }
}
