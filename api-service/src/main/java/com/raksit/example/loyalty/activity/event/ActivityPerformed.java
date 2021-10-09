package com.raksit.example.loyalty.activity.event;

import lombok.Value;

@Value
public class ActivityPerformed {

  Activity activity;
  Long performedOn;
}
