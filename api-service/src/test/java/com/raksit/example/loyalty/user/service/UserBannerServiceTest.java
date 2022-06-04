package com.raksit.example.loyalty.user.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@ExtendWith(MockitoExtension.class)
class UserBannerServiceTest {

  @Mock
  private S3Client s3Client;

  @Mock
  private ResponseBytes<GetObjectResponse> bannerBytes;

  @InjectMocks
  private UserBannerService userBannerService;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(userBannerService, "bucketName", "test-bucket");
  }

  @Test
  void shouldReturnBase64BannerImage_whenGetBase64BannerImage_givenUserId() {
    // Given
    String userId = UUID.randomUUID().toString();
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket("test-bucket")
        .key(String.format("banner-%s.png", userId))
        .build();
    when(s3Client.getObjectAsBytes(getObjectRequest)).thenReturn(bannerBytes);
    String base64Banner = "R5ay8lR8GNIaUi60FgrEjQ==";
    when(bannerBytes.asByteArray()).thenReturn(Base64.getDecoder().decode(base64Banner));

    // When
    String actual = userBannerService.getBase64BannerImage(userId);

    // Then
    assertThat(actual, equalTo(String.format("data:image/png;base64,%s", base64Banner)));
  }
}