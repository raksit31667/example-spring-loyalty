package com.raksit.example.loyalty.user;

import com.raksit.example.loyalty.annotation.IntegrationTest;
import com.raksit.example.loyalty.error.ErrorCode;
import com.raksit.example.loyalty.user.entity.User;
import com.raksit.example.loyalty.user.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class UserControllerTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  private static final String FIND_USER_BY_ID_ENDPOINT = "/users/";

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
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
  void shouldReturnBadRequest_whenFindUserById_givenInvalidUserId() throws Exception {
    mockMvc.perform(get(FIND_USER_BY_ID_ENDPOINT + "abc"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_USER_ID.name()))
        .andExpect(jsonPath("$.message").value("invalid user id"));
  }
}
