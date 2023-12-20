package com.payMyBuddy.app.dto;

import com.payMyBuddy.app.security.annotation.PasswordMatches;
import com.payMyBuddy.app.security.annotation.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@PasswordMatches
@Getter
@Setter
public class UserDTO {

   @NotEmpty
   private String firstName;
    @NotNull
    @NotEmpty
    private String lastName;
    @NotNull
    @NotEmpty
    private String password;
    @NotEmpty
    private String matchingPwd;

    @ValidEmail
    @NotEmpty
    private String email;

    private String createdAt;

    public String getMatchingPwd() {
        return matchingPwd;
    }

    private String[] roles;

}
