package com.uzair.todolist.controller;

import com.uzair.todolist.model.User;
import com.uzair.todolist.request.LoginRequest;
import com.uzair.todolist.response.AuthResponse;
import com.uzair.todolist.security.CustomUserDetailService;
import com.uzair.todolist.security.JwtUtil;
import com.uzair.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.management.remote.JMXAuthenticator;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService service, AuthenticationManager authenticationManager, CustomUserDetailService userDetailsService, JwtUtil jwtUtil) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody User request) {
        try {
            service.createUser ( request );
            return new ResponseEntity<> ( "user created successfully", HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<> ( "not created", HttpStatus.BAD_REQUEST );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate (
                    new UsernamePasswordAuthenticationToken ( request.getEmail ( ), request.getPassword ( ) ) );
            final UserDetails userDetails = userDetailsService.loadUserByUsername ( request.getEmail ( ) );
            final String jwt = jwtUtil.generateToken ( userDetails );
            return new ResponseEntity<> ( new AuthResponse ( jwt ), HttpStatus.OK );
        }
        catch ( BadCredentialsException b ) {
            return new ResponseEntity<> ( "bad credentials\n"+b.getMessage (), HttpStatus.UNAUTHORIZED );
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUser(@PathVariable Long userId) {
        try {
            User user = service.findUserByUserId ( userId );
            return new ResponseEntity<> ( user, HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST );
        }
    }
}
