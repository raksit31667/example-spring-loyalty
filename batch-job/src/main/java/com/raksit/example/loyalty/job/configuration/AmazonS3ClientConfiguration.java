package com.raksit.example.loyalty.job.configuration;

import com.amazonaws.auth.profile.internal.securitytoken.RoleInfo;
import com.amazonaws.auth.profile.internal.securitytoken.STSProfileCredentialsServiceProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class AmazonS3ClientConfiguration {

  @Profile({"dev", "prod"})
  @Primary
  @Bean
  public AmazonS3 amazonS3ForLegacyLoyaltyJob(
      AmazonS3ConfigurationProperties amazonS3ConfigurationProperties) {
    return AmazonS3ClientBuilder.standard()
        .withRegion(Regions.AP_SOUTHEAST_1)
        .withCredentials(new STSProfileCredentialsServiceProvider(new RoleInfo()
            .withRoleArn(amazonS3ConfigurationProperties.getRoleArn())
            .withRoleSessionName("example-spring-loyalty")))
        .build();
  }

  @Profile("local")
  @Primary
  @Bean
  public AmazonS3 amazonS3ForLocalStack() {
    return AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration("http://localhost:4566",
                Regions.AP_SOUTHEAST_1.getName())
        )
        .enablePathStyleAccess()
        .build();
  }
}
