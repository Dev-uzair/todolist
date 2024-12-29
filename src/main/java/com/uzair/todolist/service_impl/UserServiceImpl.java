package com.uzair.todolist.service_impl;

import com.uzair.todolist.model.User;
import com.uzair.todolist.repository.UserRepository;
import com.uzair.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createUser(User request) {
        request.setUserId ( new User ( ).getUserId ( ) );
        repository.save ( request );
    }

    @Override
    public User findUserByUserId(Long userId) {
        return repository.findById ( userId ).orElseThrow ( () -> new RuntimeException ( "id not found" ) );
    }
}
