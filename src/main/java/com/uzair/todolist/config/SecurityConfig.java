package com.uzair.todolist.config;

import com.uzair.todolist.security.CustomUserDetailService;
import com.uzair.todolist.security.JwtFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf ( AbstractHttpConfigurer::disable )
                .sessionManagement ( session->session.sessionCreationPolicy ( SessionCreationPolicy.STATELESS ) )
                .authorizeHttpRequests ( http->
                        http
                                .requestMatchers ( "/users/signup","/users/login" )
                                .permitAll ()
                                .anyRequest ()
                                .authenticated ()
                            )
                .userDetailsService (customUserDetailService )
                .addFilterBefore ( jwtFilter, UsernamePasswordAuthenticationFilter.class )
        ;

        return httpSecurity.build ( );
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager ( );
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user1 = User.builder ( )
//                .username ( "user1" )
//                .password (passwordEncoder ().encode (  "password" ))
//                .roles ( "ADMIN" )
//                .build ( );
//        UserDetails user2 = User.builder ( )
//                .username ( "user2" )
//                .password (passwordEncoder ().encode (  "password" ))
//                .roles ( "ADMIN" )
//                .build ( );
//        UserDetails user3 = User.builder ( )
//                .username ( "user3" )
//                .password (passwordEncoder ().encode (  "password" ))
//                .roles ( "ADMIN" ).build ( );
//        return new InMemoryUserDetailsManager ( user1, user2, user3 );
//
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder ( );
    }

}
