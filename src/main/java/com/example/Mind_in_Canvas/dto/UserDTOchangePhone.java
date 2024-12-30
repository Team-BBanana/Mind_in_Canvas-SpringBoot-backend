package com.example.Mind_in_Canvas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTOchangePhone {
    private final Long id;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private final String email;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "유효한 전화번호를 입력하세요. 형식: 010-1234-5678.")
    private final String phoneNumber;

    @Builder
    public UserDTOchangePhone(Long id, String email, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
