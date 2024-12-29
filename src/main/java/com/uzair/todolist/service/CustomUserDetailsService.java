package com.uzair.todolist.service;

import com.uzair.todolist.model.UserSecurity;
import com.uzair.todolist.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail ( email )
                .map ( UserSecurity::new )
                .orElseThrow ( () -> new UsernameNotFoundException ( "User not found" ) );

    }
}
