//package com.example.springsocial.entity.userRelated;
//
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.Table;
//
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToOne;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//
//@Entity
//@Table(name = "users_verification_code")
//@NoArgsConstructor
//@Data
//@AllArgsConstructor
//public class UserVerificationCode {
//
//    private static final int EXPIRATION = 60 *24;
//
//
//    private String token;
//
//    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
//    @JoinColumn(nullable = false, name = "user_id")
//    private User user;
//
//    private Date expiryDate;
//
//    private Date calculateExpiryDate(int expiryTimeInMinutes) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Timestamp(cal.getTime().getTime()));
//        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
//        return new Date(cal.getTime().getTime());
//    }
//
//}