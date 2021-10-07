package com.raksit.example.loyalty.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesisAsync;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
public class AmazonKinesisLocalConfiguration {

  @Primary
  @Bean
  public AmazonKinesisAsync amazonKinesisAsync() {
    return AmazonKinesisAsyncClientBuilder.standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", Regions.AP_SOUTHEAST_1.getName())
        )
        .build();
  }
}
