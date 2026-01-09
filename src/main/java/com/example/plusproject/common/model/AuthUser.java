package com.example.plusproject.common.model;

import com.example.plusproject.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser {

    private Long UserId;
    private UserRole role;
}

