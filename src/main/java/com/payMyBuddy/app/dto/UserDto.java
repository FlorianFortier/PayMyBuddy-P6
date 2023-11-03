package com.payMyBuddy.app.dto;

import com.payMyBuddy.app.security.PasswordMatches;
import com.payMyBuddy.app.security.ValidEmail;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import javax.validation.constraints.NotEmpty;

@PasswordMatches
@Getter
@Setter
public class UserDto {

   @NotNull
   @NotEmpty
   private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty

    private String password;
    private String matchingPwd;

    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    private String createdAt;

    public String getMatchingPwd() {
        return matchingPwd;
    }

    private String[] roles;

}
