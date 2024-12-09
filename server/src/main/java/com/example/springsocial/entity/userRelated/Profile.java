package com.example.springsocial.entity.userRelated;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.Image;
import com.example.springsocial.enums.Country;
import com.example.springsocial.util.ProjectUtil;

import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.servlet.http.Cookie;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "profiles")
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

//to fix nested recursions problem between 1-1 relationship
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "id"
)
@SQLDelete(sql = "UPDATE profiles SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Profile extends BaseEntity<Long>{


  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "birth_date", unique = false)
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  private LocalDate birthDate;

  @Column(name = "city")
  private String city;

  @Column(name = "summary")
  private String summary;

  @JsonIgnore
  @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
  private User user;

  @OneToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "profile_image_id", referencedColumnName = "id")
  @JsonIgnore
  private Image profilePicture;

  @Enumerated(EnumType.STRING)
  private Country country;

  private boolean isNotificationEmailed=true;
  private boolean isPostEmailed=true;


  public void setProfileCountry(String country){
    //if you send United States of america--- it'll become UNITED_STATES_OF_AMERICA
    // empty string: no country
    //wrong country text? assign Egypt
    if (country==null||country.trim().isEmpty()){
      this.country=null;
    }
    else if(ProjectUtil.isInEnum(Country.class,country)){
      this.country = Country.valueOf(country.replaceAll("\\s+", "_").toUpperCase());
     }
    else{
      this.country=Country.EGYPT;
     }
    }

    public String getFullName(){
    if(this.getFirstName()==null && this.getLastName()==null){
      return this.user.getEmail();
    }
    if(this.getFirstName()==null){
      return this.getLastName();
    }
    if(this.getLastName()==null){
      return this.getFirstName();
    }
      return this.getFirstName() + "-" + this.getLastName();
    }


}
