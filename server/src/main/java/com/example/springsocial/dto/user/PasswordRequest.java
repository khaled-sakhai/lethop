package com.example.springsocial.dto.user;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class PasswordRequest {

  @JsonProperty("verification_code")
  private String verificationCode;

  @NotEmpty(message = "Password must not be empty")
  @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
  private String password;
}