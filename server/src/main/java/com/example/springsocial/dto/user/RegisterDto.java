package com.example.springsocial.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisterDto {
  @Email
  @NotEmpty(message = "Email must not be empty")
  @Size(min = 4, max = 30, message = "Email must be between 4 and 30 characters")
  private String email;

  @NotEmpty(message = "Password must not be empty")
  @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
  private String password;
}
