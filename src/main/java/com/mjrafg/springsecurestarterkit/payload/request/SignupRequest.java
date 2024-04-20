package com.mjrafg.springsecurestarterkit.payload.request;

import java.util.Set;

import com.mjrafg.springsecurestarterkit.app.base.role.RoleEntity;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {
  @NotBlank
  @Size(max = 20, min = 3)
  private String userName;

  @NotBlank
  @Size(max = 50)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  private String role;

}
