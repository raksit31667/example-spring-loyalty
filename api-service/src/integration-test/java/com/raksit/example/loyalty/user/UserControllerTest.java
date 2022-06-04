package com.raksit.example.loyalty.user;

import static com.raksit.example.feature.FeatureName.EXAMPLE_SPRING_LOYALTY_FIND_USER_BY_ID;
import static com.raksit.example.loyalty.extension.AmazonS3ClientExtension.localStackContainer;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.raksit.example.feature.FeatureToggleService;
import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.error.ErrorCode;
import com.raksit.example.loyalty.user.entity.User;
import com.raksit.example.loyalty.user.repository.UserRepository;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;

@IntegrationTest
class UserControllerTest {

  @Autowired
  private UserRepository userRepository;

  @SpyBean
  @Autowired
  private FeatureToggleService featureToggleService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private S3Client s3Client;

  private MockMvc mockMvc;

  private static final String FIND_USER_BY_ID_ENDPOINT = "/users/";

  private static final String S3_BUCKET_NAME = "test-bucket";

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    s3Client.createBucket(CreateBucketRequest.builder()
        .bucket(S3_BUCKET_NAME)
        .build());
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    s3Client.deleteBucket(DeleteBucketRequest.builder()
        .bucket(S3_BUCKET_NAME)
        .build());
    Mockito.reset(featureToggleService);
  }

  @Test
  void shouldReturnUser_whenFindUserById_givenUserWithIdExists() throws Exception {
    // Given
    final User user = new User(RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10));
    user.setPoints(100L);
    final User savedUser = userRepository.save(user);
    doReturn(true)
        .when(featureToggleService).isEnabled(EXAMPLE_SPRING_LOYALTY_FIND_USER_BY_ID);

    // When
    // Then
    mockMvc.perform(get(FIND_USER_BY_ID_ENDPOINT + savedUser.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(savedUser.getId().toString()))
        .andExpect(jsonPath("$.data.firstName").value(savedUser.getFirstName()))
        .andExpect(jsonPath("$.data.lastName").value(savedUser.getLastName()))
        .andExpect(jsonPath("$.data.email").value(savedUser.getEmail()))
        .andExpect(jsonPath("$.data.phone").value(savedUser.getPhone()))
        .andExpect(jsonPath("$.data.points").value(savedUser.getPoints()));
  }

  @Test
  void shouldReturnNull_whenFindUserById_givenFeatureIsDisabled() throws Exception {
    // Given
    final User user = new User(RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10),
        RandomStringUtils.random(10));
    user.setPoints(100L);
    final User savedUser = userRepository.save(user);
    doReturn(false)
        .when(featureToggleService).isEnabled(EXAMPLE_SPRING_LOYALTY_FIND_USER_BY_ID);

    // When
    // Then
    mockMvc.perform(get(FIND_USER_BY_ID_ENDPOINT + savedUser.getId()))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnBadRequest_whenFindUserById_givenInvalidUserId() throws Exception {
    // Given
    doReturn(true)
        .when(featureToggleService).isEnabled(EXAMPLE_SPRING_LOYALTY_FIND_USER_BY_ID);

    // When
    // Then
    mockMvc.perform(get(FIND_USER_BY_ID_ENDPOINT + "abc"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_USER_ID.name()))
        .andExpect(jsonPath("$.message").value("invalid user id"));
  }

  @TestConfiguration
  static class AmazonS3ClientTestConfiguration {

    @Primary
    @Bean
    public S3Client s3ClientForLegacyLoyaltyJob() throws URISyntaxException {
      return S3Client.builder()
          .endpointOverride(new URI(
              localStackContainer.getEndpointConfiguration(Service.S3).getServiceEndpoint()))
          .credentialsProvider(new AwsCredentialsProvider() {
            @Override
            public AwsCredentials resolveCredentials() {
              return new AwsCredentials() {
                @Override
                public String accessKeyId() {
                  return localStackContainer.getAccessKey();
                }

                @Override
                public String secretAccessKey() {
                  return localStackContainer.getSecretKey();
                }
              };
            }
          })
          .build();
    }
  }
}
