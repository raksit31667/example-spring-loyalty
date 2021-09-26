package com.raksit.example.loyalty.job.configuration;

import com.amazonaws.auth.profile.internal.securitytoken.RoleInfo;
import com.amazonaws.auth.profile.internal.securitytoken.STSProfileCredentialsServiceProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3ClientConfiguration {

  @Bean
  public AmazonS3 amazonS3(AmazonS3ConfigurationProperties amazonS3ConfigurationProperties) {
    return AmazonS3ClientBuilder.standard()
        .withRegion(Regions.AP_SOUTHEAST_1)
        .withCredentials(new STSProfileCredentialsServiceProvider(new RoleInfo()
            .withRoleArn(amazonS3ConfigurationProperties.getS3RoleArn())
            .withRoleSessionName("example-spring-loyalty")))
        .build();
  }
}
