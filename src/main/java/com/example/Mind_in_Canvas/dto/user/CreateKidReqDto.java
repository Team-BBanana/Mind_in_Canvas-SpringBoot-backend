package com.example.Mind_in_Canvas.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateKidReqDto {
    @NotBlank
    private String name;
    @NotNull
    private Integer age;
}