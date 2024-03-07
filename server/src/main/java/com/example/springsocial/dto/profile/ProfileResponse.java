package com.example.springsocial.dto.profile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class ProfileResponse {
    
  private Long id;
  private String profileCreationDate;
  private String birthDate;
  private String country;
  private String city;
  private String summary;
  private UserInfo userInfo;


  public ProfileResponse(User user){
    this.id=user.getUserProfile().getId();
    this.city=user.getUserProfile().getCity();
    if(user.getUserProfile().getBirthDate() !=null){
        this.birthDate=user.getUserProfile().getBirthDate().toString();
    }
    if(user.getUserProfile().getCountry()!=null){
        this.country=user.getUserProfile().getCountry().toString();

    }

    this.summary=user.getUserProfile().getSummary();
    /// user info
    this.userInfo.setUserName(user.getUserProfile().getFullName());
    this.userInfo.setUserImageUrl(user.getUserProfile().getProfilePicture().getUrl());
    this.userInfo.setUserId(user.getId());


     // Format the lastModifiedDate to show year, month, day, and hours
        // Convert Date to LocalDateTime
        LocalDateTime localDateTime = user.getUserProfile().getCreatedDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Format the LocalDateTime to show year, month, day, and hours
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.profileCreationDate = localDateTime.format(formatter);

  }
  
}
