package com.raksit.example.loyalty.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

  private String id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Long points;
  private Long numberOfSubscriptions;
  private String bannerImage;
}
