package com.example.plusproject.domain.auth.model.response;

import com.example.plusproject.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AuthSignupResponse {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
