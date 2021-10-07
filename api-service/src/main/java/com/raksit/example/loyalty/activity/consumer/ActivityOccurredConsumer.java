package com.raksit.example.loyalty.activity.consumer;

import com.raksit.example.loyalty.activity.event.ActivityOccurred;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component("activity-occurred-consumer")
public class ActivityOccurredConsumer implements Consumer<ActivityOccurred> {

  @Override
  public void accept(ActivityOccurred activityOccurred) {
    log.info("Activity occurred event for user {} received.", activityOccurred.getActivity().getUserId());
  }
}
