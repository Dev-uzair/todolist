package com.uzair.todolist.config;

import com.uzair.todolist.security.CustomUserDetailsService;
import com.uzair.todolist.security.JwtFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtFilter jwtFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf ( AbstractHttpConfigurer::disable )
                .sessionManagement ( configurer -> configurer.sessionCreationPolicy ( SessionCreationPolicy.STATELESS ) )
                .authorizeHttpRequests ( authorizeRequest ->
                        authorizeRequest
                                .requestMatchers ( "/users/signup", "/users/login" )
                                .permitAll ( )
                                .anyRequest ( )
                                .authenticated ( ) )
                .userDetailsService ( customUserDetailsService )
                .addFilterBefore ( jwtFilter, AuthenticationFilter.class );
//                .httpBasic ( Customizer.withDefaults ( ) );
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
