package com.example.plusproject.domain.auth.controller;

import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.auth.model.request.AuthLoginRequest;
import com.example.plusproject.domain.auth.model.request.AuthSignupRequest;
import com.example.plusproject.domain.auth.model.response.AuthLoginResponse;
import com.example.plusproject.domain.auth.model.response.AuthSignupResponse;
import com.example.plusproject.domain.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<AuthSignupResponse>> signUp(@RequestBody AuthSignupRequest authSignupRequest) {
        return ResponseEntity.ok(new CommonResponse<>(true,
                "회원가입이 완료되었습니다.",
                authService.signUp(authSignupRequest))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest authLoginRequest) {
        return ResponseEntity.ok(authService.login(authLoginRequest));
    }

}
