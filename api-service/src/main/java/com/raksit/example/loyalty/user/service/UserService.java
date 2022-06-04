package com.raksit.example.loyalty.user.service;

import com.raksit.example.loyalty.user.dto.UserDTO;
import com.raksit.example.loyalty.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

  private final UserRepository userRepository;

  private final UserBannerService userBannerService;

  public UserService(UserRepository userRepository,
      UserBannerService userBannerService) {
    this.userRepository = userRepository;
    this.userBannerService = userBannerService;
  }

  public UserDTO findUserById(String userId) {
    return userRepository.findById(UUID.fromString(userId))
        .map(user -> UserDTO.builder()
            .id(userId)
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .points(user.getPoints())
            .bannerImage(userBannerService.getBase64BannerImage(userId))
            .build()
        ).orElseThrow();
  }
}
