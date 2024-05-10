package com.example.springsocial.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmailRequest {

    @Email
    @NotEmpty(message = "Email must not be empty")
    @Size(min = 4, max = 30, message = "Email must be between 4 and 30 characters")
    private String email;

}
