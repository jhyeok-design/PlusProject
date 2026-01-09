package com.example.plusproject.domain.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {

    private String name;
    private String password;
    private String nickname;
    private String phone;
    private String address;
}

