package com.example.Mind_in_Canvas.dto.user;

import com.example.Mind_in_Canvas.domain.user.kid.Kid;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UpdateKidResDto {
    private final UUID kidId;
    private final String name;
    private final Integer birthDate;
    private final UUID parentId;

    public static UpdateKidResDto from(Kid kid) {
        return UpdateKidResDto.builder()
                .kidId(kid.getKidId())
                .name(kid.getName())
                .birthDate(kid.getAge())
                .parentId(kid.getParent().getParentId())
                .build();
    }
}