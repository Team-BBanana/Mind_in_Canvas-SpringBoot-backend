package com.example.Mind_in_Canvas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDTO {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private final String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(min = 2, max = 100, message = "이름은 2자 이상 100자 이하로 입력해야 합니다.")
    private final String name;

    @Pattern(regexp = "GOOGLE|NONE", message = "유효한 소셜 제공자를 선택해야 합니다.")
    private final String socialProvider;

    @NotBlank(message = "사용자 역할은 필수 값입니다.")
    @Pattern(regexp = "ROLE_USER", message = "유효한 사용자 역할을 선택해야 합니다.")
    private final String role;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "유효한 전화번호를 입력하세요. 형식: 010-1234-5678.")
    private final String phoneNumber;


    @Builder
    public UserDTO(String email, String password, String name,
                   String socialProvider, String role, String phoneNumber, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.socialProvider = socialProvider;
        this.role = role;
        this.phoneNumber = phoneNumber;

    }
}
