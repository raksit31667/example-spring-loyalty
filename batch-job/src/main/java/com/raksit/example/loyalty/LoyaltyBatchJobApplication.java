package com.raksit.example.loyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableConfigurationProperties
@SpringBootApplication
public class LoyaltyBatchJobApplication {

  public static void main(String[] args) {
    System.exit(SpringApplication.exit(SpringApplication.run(LoyaltyBatchJobApplication.class, args)));
  }
}
