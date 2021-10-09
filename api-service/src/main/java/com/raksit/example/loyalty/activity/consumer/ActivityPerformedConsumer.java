package com.raksit.example.loyalty.activity.consumer;

import com.raksit.example.loyalty.activity.event.ActivityPerformed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component("activity-performed-consumer")
public class ActivityPerformedConsumer implements Consumer<ActivityPerformed> {

  @Override
  public void accept(ActivityPerformed activityPerformed) {
    log.info("Activity occurred event for user {} received.", activityPerformed.getActivity().getUserId());
  }
}
