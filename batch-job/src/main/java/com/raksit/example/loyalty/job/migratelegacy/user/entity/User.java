package com.raksit.example.loyalty.job.migratelegacy.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {

  @Id
  @GeneratedValue
  private UUID id;

  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Long activityPoints;

  @Transient
  private Boolean isActive;

  public User(String firstName, String lastName, String email, String phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.activityPoints = 0L;
  }
}
