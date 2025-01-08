package com.uzair.todolist.controller;

import com.uzair.todolist.model.User;
import com.uzair.todolist.request.UserRequest;
import com.uzair.todolist.security.CustomUserDetailService;
import com.uzair.todolist.security.JwtUtil;
import com.uzair.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
        public ResponseEntity<String> userLogin(@RequestBody UserRequest request) {

        try {

             authenticationManager.authenticate (
                    new UsernamePasswordAuthenticationToken ( request.getEmail ( ), request.getPassword ( ) ));
            UserDetails userDetails = userDetailsService.loadUserByUsername ( request.getEmail ( ) );
            String jwtToken = jwtUtil.generateToken ( userDetails );
            return new ResponseEntity<> ( jwtToken, HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<> ( e.getMessage ( ), HttpStatus.BAD_REQUEST );
        }
    }





    @GetMapping("/{userId}")
    public ResponseEntity<User> findUser(@PathVariable Long userId) {
        try {
            User user = service.findUserByUserId ( userId );
            return new ResponseEntity<> ( user, HttpStatus.OK );
        }
        catch ( Exception e ) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST );
        }
    }
}
