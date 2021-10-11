package com.raksit.example.loyalty.activity.event;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ActivityPerformed {

  Activity activity;
  Long performedOn;
}
