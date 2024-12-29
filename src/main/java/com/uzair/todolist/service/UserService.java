package com.uzair.todolist.service;


import com.uzair.todolist.model.User;

public interface UserService {
    void createUser(User request);

    User findUserByUserId(Long userId);
}
