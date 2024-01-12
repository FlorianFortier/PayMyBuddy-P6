package com.payMyBuddy.app.security;


import com.payMyBuddy.app.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final CustomUserDetailsService myUserDetailsService;


    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    public SecurityConfig(CustomUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults())
                .csrf(withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/registration", "/login","/registrationConfirm", "/static", "/css/**", "/js/**", "/images/**", "/fonts/**", "/transfer.html", "/transfer.html").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/transfer.html")
                        .permitAll()
                )
                .logout(logout ->
                        logout
                            .logoutSuccessUrl("/login.html?logout=true")
                            .permitAll()
                );
        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }



    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(
                "This is the test email template for your email:\n%s\n");
        return message;
    }

}
