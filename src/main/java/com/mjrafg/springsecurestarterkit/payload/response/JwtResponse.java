package com.mjrafg.springsecurestarterkit.payload.response;

import com.mjrafg.springsecurestarterkit.app.base.user.UserEntity;
import lombok.Data;

@Data
public class JwtResponse {
  private String accessToken;
  private String type = "Bearer";
  private UserEntity userData;

  public JwtResponse(String accessToken, UserEntity userData) {
    this.accessToken = accessToken;
    this.userData = userData;
  }
}
