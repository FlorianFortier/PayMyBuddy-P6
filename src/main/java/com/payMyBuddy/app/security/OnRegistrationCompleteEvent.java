package com.payMyBuddy.app.security;

import com.payMyBuddy.app.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private User user;
    private String token;

    public OnRegistrationCompleteEvent(
            User user, Locale locale, String appUrl, String token) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.token = token;
    }

}