package com.example.Mind_in_Canvas.dto.user;

import com.example.Mind_in_Canvas.domain.user.parent.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class UpdateUserResDto {
    private final UUID parentId;
    private final String username;
    private final String email;
    private final String phoneNumber;
    private final LocalDateTime updatedAt;

    @Builder
    public UpdateUserResDto(User user) {
        this.parentId = user.getParentId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.updatedAt = user.getUpdatedAt();
    }
}

