package com.example.plusproject.domain.user.controller;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.user.model.request.UserUpdateRequest;
import com.example.plusproject.domain.user.model.response.UserReadResponse;
import com.example.plusproject.domain.user.model.response.UserUpdateResponse;
import com.example.plusproject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<CommonResponse<UserReadResponse>> mypage(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(userService.myPage(authUser));
    }

    @PutMapping
    public ResponseEntity<CommonResponse<UserUpdateResponse>> infoUpdate(@AuthenticationPrincipal AuthUser authUser,
                                                                         @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.infoUpdate(authUser, userUpdateRequest));
    }

    @DeleteMapping("/me")
    public ResponseEntity<CommonResponse<?>> deleteUser(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(userService.deleteUser(authUser));
    }
}
