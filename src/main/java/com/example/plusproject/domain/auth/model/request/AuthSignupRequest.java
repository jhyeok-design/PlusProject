package com.example.plusproject.domain.auth.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthSignupRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String address;

}
