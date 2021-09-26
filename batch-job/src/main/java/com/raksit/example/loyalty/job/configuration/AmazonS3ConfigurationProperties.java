package com.raksit.example.loyalty.job.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class AmazonS3ConfigurationProperties {

  private String s3RoleArn;

}
