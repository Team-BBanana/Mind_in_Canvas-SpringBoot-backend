package com.example.Mind_in_Canvas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KidDTO {
    private final String name;

    private final Integer age;

    @Builder
    public KidDTO(String name, Integer age){
        this.name = name;
        this.age = age;
    }

}
