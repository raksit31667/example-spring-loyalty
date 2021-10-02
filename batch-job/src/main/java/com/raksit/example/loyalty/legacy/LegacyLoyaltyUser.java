package com.raksit.example.loyalty.legacy;

import lombok.Value;

@Value
public class LegacyLoyaltyUser {

  String id;
  String firstName;
  String lastName;
  String email;
  String phone;
  Boolean isActive;
}
