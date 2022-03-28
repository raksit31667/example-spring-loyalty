package com.raksit.example.loyalty.user.controller;

import com.raksit.example.feature.FeatureToggleService;
import com.raksit.example.loyalty.response.Response;
import com.raksit.example.loyalty.user.dto.UserDTO;
import com.raksit.example.loyalty.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.raksit.example.feature.FeatureName.EXAMPLE_SPRING_LOYALTY_FIND_USER_BY_ID;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

  private final UserService userService;

  private final FeatureToggleService featureToggleService;

  public UserController(UserService userService, FeatureToggleService splitFeatureToggleService) {
    this.userService = userService;
    this.featureToggleService = splitFeatureToggleService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Response<UserDTO>> findUserById(@PathVariable @ValidUserId String userId) {
    if (featureToggleService.isEnabled(EXAMPLE_SPRING_LOYALTY_FIND_USER_BY_ID)) {
      return ResponseEntity.ok(new Response<>(userService.findUserById(userId)));
    }
    return ResponseEntity.noContent().build();
  }
}
