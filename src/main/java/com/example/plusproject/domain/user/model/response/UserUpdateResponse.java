package com.example.plusproject.domain.user.model.response;

import com.example.plusproject.domain.user.model.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserUpdateResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final String nickname;
    private final String phone;
    private final String address;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserUpdateResponse from(UserDto user) {
        return new UserUpdateResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getPhone(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}