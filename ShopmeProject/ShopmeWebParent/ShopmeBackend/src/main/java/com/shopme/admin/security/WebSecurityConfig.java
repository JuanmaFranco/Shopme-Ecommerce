package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailsService();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(customizer -> customizer
                .requestMatchers(
                        "/images/**",
                        "/js/**",
                        "/webjars/**",
                        "/style.css",
                        "/fontawesome/**"
                )
                .permitAll()
        );

        http.authorizeHttpRequests(configurer -> configurer
                .anyRequest()
                .authenticated()
        );

        http.authenticationProvider(authenticationProvider());

        http.formLogin(configurer -> configurer
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
        );

        http.logout(LogoutConfigurer::permitAll);

        http.rememberMe(configurer -> configurer
                .key("AbcDefgHijKlmnOpqrs_1234567890")
                .tokenValiditySeconds(60 * 60 * 24 * 7)  // one week
                .userDetailsService(userDetailsService()));

        return http.build();
    }

}
