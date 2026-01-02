package com.example.plusproject.domain.auth.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginRequest {
    private String email;
    private String password;
}
