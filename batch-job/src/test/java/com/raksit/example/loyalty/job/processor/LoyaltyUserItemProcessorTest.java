package com.raksit.example.loyalty.job.processor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.raksit.example.loyalty.legacy.LegacyLoyaltyClient;
import com.raksit.example.loyalty.legacy.LegacyLoyaltyUser;
import com.raksit.example.loyalty.user.User;
import java.util.UUID;
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
  void shouldReturnUserWithLoyaltyPointsFromLegacySystem_whenProcess_givenUserReadFromDatabase()
      throws Exception {
    // Given
    final UUID userId = UUID.randomUUID();
    User userFromDatabase = new User("John", "Doe", "john.doe@example.com", "+6678901234");
    userFromDatabase.setId(userId);
    when(legacyLoyaltyClient.findUserById(userId.toString())).thenReturn(
        new LegacyLoyaltyUser(userId.toString(), 100L));

    // When
    User actual = loyaltyUserItemProcessor.process(userFromDatabase);

    // Then
    assertThat(actual.getId(), equalTo(userId));
    assertThat(actual.getFirstName(), equalTo("John"));
    assertThat(actual.getLastName(), equalTo("Doe"));
    assertThat(actual.getEmail(), equalTo("john.doe@example.com"));
    assertThat(actual.getPhone(), equalTo("+6678901234"));
    assertThat(actual.getPoints(), equalTo(100L));
  }
}