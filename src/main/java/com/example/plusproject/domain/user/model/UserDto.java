package com.example.plusproject.domain.user.model;

import com.example.plusproject.common.enums.UserRole;
import com.example.plusproject.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserDto {

    private final Long id;
    private final String name;
    private final String email;
    private final String nickname;
    private final String phone;
    private final String address;
    private final UserRole role;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNickname(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.isDeleted(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}