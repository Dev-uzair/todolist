package com.uzair.todolist.service_impl;

import com.uzair.todolist.model.User;
import com.uzair.todolist.repository.UserRepository;
import com.uzair.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void createUser(User request) {
        request.setUserId ( new User ( ).getUserId ( ) );
        request.setPassword ( encoder.encode (request.getPassword ( )) );
        repository.save ( request );
    }

    @Override
    public User findUserByUserId(Long userId) {
        return repository.findById ( userId ).orElseThrow ( () -> new RuntimeException ( "id not found" ) );
    }
}
