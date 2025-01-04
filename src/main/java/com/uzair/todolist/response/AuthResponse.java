package com.uzair.todolist.response;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    public AuthResponse(String token) {
        this.token=token;
    }
}
