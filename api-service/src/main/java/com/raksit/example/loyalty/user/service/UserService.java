package com.raksit.example.loyalty.user.service;

import com.raksit.example.loyalty.user.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

  private final UserDomainService userDomainService;

  private final UserBannerService userBannerService;

  public UserService(UserDomainService userDomainService,
      UserBannerService userBannerService) {
    this.userDomainService = userDomainService;
    this.userBannerService = userBannerService;
  }

  public UserDTO findUserById(String userId) {
    return userDomainService.findUserById(UUID.fromString(userId))
        .map(user -> UserDTO.builder()
            .id(userId)
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .numberOfSubscriptions(user.getNumberOfSubscriptions())
            .points(user.getTotalPoints())
            .bannerImage(userBannerService.getBase64BannerImage(userId))
            .build()
        ).orElseThrow();
  }
}
