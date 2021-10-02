package com.raksit.example.loyalty.job.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class AmazonS3ConfigurationProperties {

  private String roleArn;

  private String bucketName;
}
