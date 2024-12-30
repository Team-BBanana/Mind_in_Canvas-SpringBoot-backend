package com.example.Mind_in_Canvas.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateUserReqDto {
    private String password;
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    private String phoneNumber;
}
