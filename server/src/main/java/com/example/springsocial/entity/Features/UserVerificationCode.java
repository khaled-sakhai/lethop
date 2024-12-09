package com.example.springsocial.entity.Features;

import javax.persistence.*;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.VerificationType;
import com.example.springsocial.util.ProjectUtil;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import javax.persistence.Table;


import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "user_verification_codes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE user_verification_codes SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class UserVerificationCode extends BaseEntity<Long> {

   

    @Column(name="confirmation_code")
    private String confirmationCode;

    @Enumerated(EnumType.STRING)
    private VerificationType type;

    private boolean isConfirmed=false;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public UserVerificationCode(User user, VerificationType type) {
        this.user = user;
        this.type=type;
        this.confirmationCode = ProjectUtil.generateRandomId();
    }

    // getters and setters
}