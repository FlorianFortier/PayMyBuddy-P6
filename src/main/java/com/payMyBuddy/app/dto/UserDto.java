package com.payMyBuddy.app.dto;

import com.payMyBuddy.app.security.PasswordMatches;
import com.payMyBuddy.app.security.ValidEmail;
import org.antlr.v4.runtime.misc.NotNull;

import javax.validation.constraints.NotEmpty;

@PasswordMatches
public class UserDto {

   @NotNull
   @NotEmpty
   private String firstName;

    @NotNull
    @NotEmpty

    private String surname;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPwd;
    }

    public void setMatchingPwd(String matchingPwd) {
        this.matchingPwd = matchingPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
