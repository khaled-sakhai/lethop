package com.example.springsocial.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

  @NotEmpty(message = "firstName must not be empty")
  @Size(min = 3, max = 15, message = "firstName must be between 3 and 15 characters")
  @JsonProperty("first_name")
  private String firstName;

  @NotEmpty(message = "lastName must not be empty")
  @Size(min = 3, max = 15, message = "lastName must be between 3 and 15 characters")
  @JsonProperty("last_name")
  private String lastName;

  
  @JsonProperty("birth_date")
  private LocalDate birthDate;

  private String country;

  private String city;

  @Size(min = 0, max = 400, message = "summary must be between 0 and 400 characters")
  private String summary;
}
