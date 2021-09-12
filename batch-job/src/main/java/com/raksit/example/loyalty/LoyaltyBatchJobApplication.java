package com.raksit.example.loyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class LoyaltyBatchJobApplication {

  public static void main(String[] args) {
    SpringApplication.run(LoyaltyBatchJobApplication.class, args);
  }
}
