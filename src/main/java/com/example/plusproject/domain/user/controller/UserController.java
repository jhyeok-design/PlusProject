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
    public ResponseEntity<CommonResponse<UserReadResponse>> readUser(@AuthenticationPrincipal AuthUser authUser) {

        return ResponseEntity.ok(CommonResponse.success(
                "마이페이지 조회 성공",
                userService.readMypage(authUser)
                )
        );
    }

    @PutMapping
    public ResponseEntity<CommonResponse<UserUpdateResponse>> updateUser(@AuthenticationPrincipal AuthUser authUser,
                                                                         @RequestBody UserUpdateRequest userUpdateRequest) {

        return ResponseEntity.ok(CommonResponse.success(
                "회원 정보 수정이 완료되었습니다",
                userService.updateUser(authUser, userUpdateRequest)
                )
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<CommonResponse<?>> deleteUser(@AuthenticationPrincipal AuthUser authUser) {

        return ResponseEntity.ok(CommonResponse.success(
                "회원 탈퇴가 완료되었습니다",
                null
                )
        );
    }
}
