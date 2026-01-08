package com.example.plusproject.domain.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthSignupRequest {

    @Email
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "이메일 형식이 올바르지 않습니다."
    )
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @Pattern(
            regexp = "^01[016789]-\\d{3,4}-\\d{4}$",
            message = "휴대폰 형식이 올바르지 않습니다. (01x-xxxx-xxxx)"
    )
    @NotBlank
    private String phone;

    @NotBlank
    private String address;
}