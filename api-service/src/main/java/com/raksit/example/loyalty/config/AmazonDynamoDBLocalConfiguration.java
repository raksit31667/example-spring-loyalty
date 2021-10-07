package com.raksit.example.loyalty.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
public class AmazonDynamoDBLocalConfiguration {

  @Primary
  @Bean
  public AmazonDynamoDBAsync amazonDynamoDBAsync() {
    return AmazonDynamoDBAsyncClientBuilder.standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", Regions.AP_SOUTHEAST_1.getName())
        )
        .build();
  }
}
