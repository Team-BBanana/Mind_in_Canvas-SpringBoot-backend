package com.example.Mind_in_Canvas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTOchangeSocial {
    private final Long id;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private final String email;

    @Pattern(regexp = "GOOGLE|NONE", message = "유효한 소셜 제공자를 선택해야 합니다.")
    private final String socialProvider;

    @Builder
    public UserDTOchangeSocial(Long id, String email, String socialProvider) {
        this.id = id;
        this.email = email;
        this.socialProvider = socialProvider;
    }
}