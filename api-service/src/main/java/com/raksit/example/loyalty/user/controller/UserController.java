package com.raksit.example.loyalty.user.controller;

import com.raksit.example.loyalty.response.Response;
import com.raksit.example.loyalty.user.dto.UserDTO;
import com.raksit.example.loyalty.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  public Response<UserDTO> findUserById(@PathVariable String userId) {
    return new Response<>(userService.findUserById(userId));
  }
}
