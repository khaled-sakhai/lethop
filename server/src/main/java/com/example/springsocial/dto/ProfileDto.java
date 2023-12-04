package com.example.springsocial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
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

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("birth_date")
  private LocalDate birthDate;

  private String country;

  private String city;

  private String summary;
}
