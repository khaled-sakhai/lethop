package com.example.springsocial.dto.profile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.util.ProjectUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class ProfileResponse implements Serializable {
    
  private Long id;
  private String profileCreationDate;
  private String birthDate;
  private String country;
  private String city;
  private String summary;
  private UserInfo userInfo;


  public ProfileResponse(User user){
    if (user.getUserProfile()!=null) {
        this.id = user.getUserProfile().getId();
        this.city = user.getUserProfile().getCity();
        if (user.getUserProfile().getBirthDate() != null) {
            this.birthDate = user.getUserProfile().getBirthDate().toString();
        }
        if (user.getUserProfile().getCountry() != null) {
            this.country = user.getUserProfile().getCountry().name();
        }
        this.summary = user.getUserProfile().getSummary();
        /// user info
        this.userInfo=new UserInfo(user);

        this.profileCreationDate = ProjectUtil.convertDateToString(user.getCreatedDate());
    }
  }
  
}
