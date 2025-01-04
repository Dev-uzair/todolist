package com.uzair.todolist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailService customUserDetailService, JwtFilter jwtFilter) {
        this.customUserDetailService = customUserDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf ( AbstractHttpConfigurer::disable )
                .sessionManagement ( session -> session.
                        sessionCreationPolicy ( SessionCreationPolicy.STATELESS ) )
                .authorizeHttpRequests (
                        auth -> auth
                                .requestMatchers ( "/users/signup", "/users/login" )
                                .permitAll ( )
                                .anyRequest ( )
                                .authenticated ( )
                )
                .userDetailsService ( customUserDetailService )
                .addFilterBefore ( jwtFilter, AuthenticationFilter.class )

        ;

        return http.build ( );
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager ( );
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder ( );
    }
}
