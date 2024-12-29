package com.uzair.todolist.controller;

import com.uzair.todolist.model.User;
import com.uzair.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
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
