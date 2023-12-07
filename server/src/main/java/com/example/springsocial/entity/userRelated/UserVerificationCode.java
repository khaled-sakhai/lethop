package com.example.springsocial.entity.userRelated;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.util.ProjectUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserVerificationCode extends BaseEntity<Long> {

   

    @Column(name="confirmation_code")
    private String confirmationCode;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;



    public UserVerificationCode(User user) {
        this.user = user;
        confirmationCode = ProjectUtil.generateRandomId();
    }

    // getters and setters
}