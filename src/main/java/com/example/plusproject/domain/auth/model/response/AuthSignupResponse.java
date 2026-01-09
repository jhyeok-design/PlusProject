package com.example.plusproject.domain.auth.model.response;

import com.example.plusproject.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AuthSignupResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final String nickname;
    private final String phone;
    private final String address;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static AuthSignupResponse from(User user) {
        return new AuthSignupResponse(
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
