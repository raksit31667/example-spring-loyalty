package com.raksit.example.loyalty.job.processor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.raksit.example.loyalty.legacy.LegacyLoyaltyClient;
import com.raksit.example.loyalty.legacy.LegacyLoyaltyUser;
import com.raksit.example.loyalty.legacy.LoyaltyTransaction;
import com.raksit.example.loyalty.user.User;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoyaltyUserItemProcessorTest {

  @Mock
  private LegacyLoyaltyClient legacyLoyaltyClient;

  @InjectMocks
  private LoyaltyUserItemProcessor loyaltyUserItemProcessor;

  @Test
  void shouldReturnUserWithLoyaltyPointsFromLegacySystem_whenProcess_givenLoyaltyTransactionAndUserActiveInLegacy() {
    // Given
    final String userId = RandomStringUtils.random(10);
    final LoyaltyTransaction loyaltyTransaction = new LoyaltyTransaction(userId, 100L);
    final LegacyLoyaltyUser legacyLoyaltyUser = new LegacyLoyaltyUser(userId, "John", "Doe",
        "john.doe@example.com", "+6678901234", true);
    when(legacyLoyaltyClient.findUserById(userId)).thenReturn(legacyLoyaltyUser);

    // When
    User actual = loyaltyUserItemProcessor.process(loyaltyTransaction);

    // Then
    assertThat(actual.getFirstName(), equalTo("John"));
    assertThat(actual.getLastName(), equalTo("Doe"));
    assertThat(actual.getEmail(), equalTo("john.doe@example.com"));
    assertThat(actual.getPhone(), equalTo("+6678901234"));
    assertThat(actual.getPoints(), equalTo(100L));
    assertThat(actual.getIsActive(), equalTo(true));
  }

  @Test
  void shouldReturnUserWithIsActiveFalse_whenProcess_givenLoyaltyTransactionAndUserInactiveInLegacy() {
    // Given
    final String userId = RandomStringUtils.random(10);
    final LoyaltyTransaction loyaltyTransaction = new LoyaltyTransaction(userId, 100L);
    final LegacyLoyaltyUser legacyLoyaltyUser = new LegacyLoyaltyUser(userId, "John", "Doe",
        "john.doe@example.com", "+6678901234", false);
    when(legacyLoyaltyClient.findUserById(userId)).thenReturn(legacyLoyaltyUser);

    // When
    User actual = loyaltyUserItemProcessor.process(loyaltyTransaction);

    // Then
    assertThat(actual.getFirstName(), equalTo("John"));
    assertThat(actual.getLastName(), equalTo("Doe"));
    assertThat(actual.getEmail(), equalTo("john.doe@example.com"));
    assertThat(actual.getPhone(), equalTo("+6678901234"));
    assertThat(actual.getPoints(), equalTo(100L));
    assertThat(actual.getIsActive(), equalTo(false));
  }
}