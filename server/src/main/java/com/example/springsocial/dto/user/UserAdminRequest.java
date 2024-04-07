package com.example.springsocial.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@Data
public class UserAdminRequest implements Serializable {

    @Email
    @NotEmpty(message = "Email must not be empty")
    @Size(min = 4, max = 30, message = "Email must be between 4 and 30 characters")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    @JsonProperty("is_active")
    private boolean isActive=false;

    @JsonProperty("need_profile_update")
    private boolean needProfileUpdate = true;

}
