package com.example.plusproject.domain.user.controller;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.user.model.request.UserUpdateRequest;
import com.example.plusproject.domain.user.model.response.UserReadResponse;
import com.example.plusproject.domain.user.model.response.UserUpdateResponse;
import com.example.plusproject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 유저 마이페이지 조회
     * */
    @GetMapping
    public ResponseEntity<CommonResponse<UserReadResponse>> readUser(@AuthenticationPrincipal AuthUser authUser) {

        return ResponseEntity.ok(CommonResponse.success(
                "마이페이지 조회 성공",
                userService.readMypage(authUser)
                )
        );
    }

    /**
     * 유저 정보 수정
     * */
    @PatchMapping
    public ResponseEntity<CommonResponse<UserUpdateResponse>> updateUser(@AuthenticationPrincipal AuthUser authUser,
                                                                         @RequestBody UserUpdateRequest userUpdateRequest) {

        return ResponseEntity.ok(CommonResponse.success(
                "회원 정보 수정이 완료되었습니다",
                userService.updateUser(authUser, userUpdateRequest)
                )
        );
    }

    /**
     * 유저 정보 삭제
     * */
    @DeleteMapping("/me")
    public ResponseEntity<CommonResponse<?>> deleteUser(@AuthenticationPrincipal AuthUser authUser) {

        userService.deleteUser(authUser);

        return ResponseEntity.ok(CommonResponse.success(
                "회원 탈퇴가 완료되었습니다",
                null
                )
        );
    }

    /**
     * 유저 검색
     * */
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<?>> readUserByQuery(@AuthenticationPrincipal AuthUser authUser,
                                                             @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                             @RequestParam(required = false) String domain,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false)LocalDateTime createdAt
                                                             ) {

        return ResponseEntity.ok(CommonResponse.success(
                "유저 목록 조회 성공",
                userService.readUserByQuery(authUser, pageable, domain, name, createdAt)
        ));
    }
}
