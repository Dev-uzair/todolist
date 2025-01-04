package com.uzair.todolist.service_impl;

import com.uzair.todolist.model.User;
import com.uzair.todolist.repository.UserRepository;
import com.uzair.todolist.service.UserService;
import com.uzair.todolist.util.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(User request) {
        request.setUserId ( new User ( ).getUserId ( ) );
        request.setPassword ( passwordEncoder.encode(request.getPassword ()) );
        request.setRole ( Role.USER );

        repository.save ( request );
    }

    @Override
    public User findUserByUserId(Long userId) {
        return repository.findById ( userId ).orElseThrow ( () -> new RuntimeException ( "id not found" ) );
    }
}
