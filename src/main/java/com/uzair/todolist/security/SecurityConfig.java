package com.uzair.todolist.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println ( "1= "  );
        http    .csrf ( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests ( auth -> auth
                        .anyRequest ( ).authenticated ( )

                )
                .httpBasic ( withDefaults ( ) )
//                .formLogin ( withDefaults ( ) )
        ;
        System.out.println ( "2= "  );
        return http.build ( );
    }

    @Bean
    public UserDetailsService userDetailsService() {
        System.out.println ( "3 "  );
        UserDetails user1 = User.builder ( )
                .username ( "uzair" )
                .password ( passwordEncoder ( ).encode ( "12345" ) )
                .roles ( "USER" )
                .build ( );

        UserDetails user2 = User.builder ( )
                .username ( "tayyab" )
                .password ( passwordEncoder ( ).encode ( "12345" ) )
                .roles ( "USER", "ADMIN" )
                .build ( );

        return new InMemoryUserDetailsManager ( user1, user2 );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println ( "4 "  );
        return new BCryptPasswordEncoder ( );
    }
}
