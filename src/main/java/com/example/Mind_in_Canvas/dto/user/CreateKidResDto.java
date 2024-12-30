package com.example.Mind_in_Canvas.dto.user;

import com.example.Mind_in_Canvas.domain.user.kid.Kid;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateKidResDto {
    private final UUID kidId;
    private final String name;
    private final Integer age;
    private final UUID parentId;

    public static CreateKidResDto from(Kid kid) {
        return CreateKidResDto.builder()
                .kidId(kid.getKidId())
                .name(kid.getName())
                .age(kid.getAge())
                .parentId(kid.getParent().getParentId())
                .build();
    }
}

