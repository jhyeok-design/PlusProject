package com.example.plusproject.domain.user.model.response;
import com.example.plusproject.domain.user.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserUpdateResponse {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
