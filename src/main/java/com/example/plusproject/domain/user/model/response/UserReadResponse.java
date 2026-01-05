package com.example.plusproject.domain.user.model.response;
import com.example.plusproject.domain.user.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserReadResponse {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserReadResponse from(UserDto user) {
        return new UserReadResponse(
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