package com.example.springsocial.entity.userRelated;

import java.util.Date;

import javax.persistence.*;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.util.ProjectUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "UserVerificationCodes")

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE UserVerificationCodes SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
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