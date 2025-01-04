package com.uzair.todolist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                .authorizeHttpRequests ( auth ->
                        auth
                                .anyRequest ( )
//                                .permitAll ( ) )
                                .authenticated ())
                .userDetailsService ( customUserDetailService )
                .httpBasic ( Customizer.withDefaults ( ) );

        return http.build ( );
    }


    //@Bean
//    public UserDetailsService userDetailsService(){
//     UserDetails user1= User.builder ()
//             .username ( "uzair" )
//             .password ( passwordEncoder ().encode ( "password" ) )
////             .password ( "{noop}password" )
//             .roles ( "USER" )
//             .build ();
//     return new InMemoryUserDetailsManager ( user1 );
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder ( );
    }

}
