package com.raksit.example.loyalty.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.util.Base64;

@Slf4j
@Service
public class UserBannerService {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket-name}")
  private String bucketName;

  public UserBannerService(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  public String getBase64BannerImage(String userId) {
    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key(String.format("banner-%s.png", userId))
          .build();
      byte[] bannerBytes = s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
      return String.format("data:image/png;base64,%s",
          Base64.getEncoder().encodeToString(bannerBytes));
    } catch (AwsServiceException | SdkClientException e) {
      log.error("Unable to download banner from S3", e);
      return "";
    }
  }
}
