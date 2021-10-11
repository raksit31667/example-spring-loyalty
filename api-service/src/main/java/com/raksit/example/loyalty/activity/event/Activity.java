package com.raksit.example.loyalty.activity.event;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Activity {

  String id;
  String name;
  String userId;
}
