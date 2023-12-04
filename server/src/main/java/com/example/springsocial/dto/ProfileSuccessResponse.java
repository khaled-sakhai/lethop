package com.example.springsocial.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class ProfileSuccessResponse {
    
  private Long id;
 
  private String firstName;

  private String lastName;

  private LocalDate birthDate;

  private String country;

  private String city;

  private String summary;

  private String profilePictureUrl;
}
