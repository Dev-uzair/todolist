package com.uzair.todolist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf ( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests (
                        auth -> auth
                                .requestMatchers ( "/users/signup" )
                                .permitAll ()
                                .anyRequest ( )
                                .authenticated ( )

                )
                .userDetailsService ( customUserDetailService )
                .httpBasic ( withDefaults ( ) )
//                .formLogin ( withDefaults ( ) )
        ;

        return http.build ( );
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User.builder ( )
//                .username ( "uzair" )
//                .password ( passwordEncoder ( ).encode ( "12345" ) )
//                .roles ( "USER" )
//                .build ( );
//
//        UserDetails user2 = User.builder ( )
//                .username ( "tayyab" )
//                .password ( passwordEncoder ( ).encode ( "12345" ) )
//                .roles ( "USER", "ADMIN" )
//                .build ( );
//
//        return new InMemoryUserDetailsManager ( user1, user2 );
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println ( "4 " );
        return new BCryptPasswordEncoder ( );
    }
}
