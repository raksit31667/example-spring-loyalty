package com.raksit.example.loyalty.transaction.entity;

import com.raksit.example.loyalty.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Transaction {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne
  private User user;

  private Long activityPoints;

  private Instant performedOn;

  @CreationTimestamp
  private Instant createdAt;

  public Transaction(User user, Long activityPoints, Instant performedOn) {
    this.user = user;
    this.activityPoints = activityPoints;
    this.performedOn = performedOn;
  }
}
