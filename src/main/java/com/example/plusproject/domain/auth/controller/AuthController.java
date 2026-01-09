package com.example.plusproject.domain.auth.controller;

import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.auth.model.request.AuthLoginRequest;
import com.example.plusproject.domain.auth.model.request.AuthSignupRequest;
import com.example.plusproject.domain.auth.model.response.AuthLoginResponse;
import com.example.plusproject.domain.auth.model.response.AuthSignupResponse;
import com.example.plusproject.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<AuthSignupResponse>> signUp(@Valid @RequestBody AuthSignupRequest authSignupRequest) {

        AuthSignupResponse response = authService.signUp(authSignupRequest);

        return ResponseEntity.ok(CommonResponse.success("회원가입 완료", response));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<AuthLoginResponse>> login(@Valid @RequestBody AuthLoginRequest authLoginRequest) {

        AuthLoginResponse response = authService.login(authLoginRequest);

        return ResponseEntity.ok(CommonResponse.success("로그인 완료", response));
    }
}

