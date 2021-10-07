package com.raksit.example.loyalty.activity.event;

import lombok.Value;

@Value
public class ActivityOccurred {

  Activity activity;
  Long occurredOn;
}
