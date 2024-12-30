package com.example.Mind_in_Canvas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserDTOchangeUsername {
    private final Long id;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private final String email;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(min = 2, max = 100, message = "이름은 2자 이상 100자 이하로 입력해야 합니다.")
    private final String name;

    @Builder
    public UserDTOchangeUsername(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}

