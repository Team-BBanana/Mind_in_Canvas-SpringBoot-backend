package com.example.Mind_in_Canvas.shared.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUsernameNotFoundException extends UsernameNotFoundException {

    private final String username;

    public CustomUsernameNotFoundException(String username) {
        super(String.format("{\"message\": \"사용자를 찾을 수 없습니다\", \"user\": \"%s\"}", username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
