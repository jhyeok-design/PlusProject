package com.example.plusproject.domain.user.controller;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.user.model.request.UserUpdateRequest;
import com.example.plusproject.domain.user.model.response.UserReadResponse;
import com.example.plusproject.domain.user.model.response.UserUpdateResponse;
import com.example.plusproject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
     */
    @GetMapping
    public ResponseEntity<CommonResponse<UserReadResponse>> readUser(@AuthenticationPrincipal AuthUser authUser) {

        UserReadResponse response = userService.readMypage(authUser);

        return ResponseEntity.ok(CommonResponse.success("마이페이지 조회 성공", response));
    }

    /**
     * 유저 정보 수정
     */
    @PatchMapping
    public ResponseEntity<CommonResponse<UserUpdateResponse>> updateUser(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserUpdateRequest userUpdateRequest) {

        UserUpdateResponse result = userService.updateUser(authUser, userUpdateRequest);

        return ResponseEntity.ok(CommonResponse.success("회원 정보 수정이 완료되었습니다", result));
    }

    /**
     * 유저 정보 삭제
     */
    @DeleteMapping("/me")
    public ResponseEntity<CommonResponse<?>> deleteUser(@AuthenticationPrincipal AuthUser authUser) {

        userService.deleteUser(authUser);

        return ResponseEntity.ok(CommonResponse.success("회원 탈퇴가 완료되었습니다", null));
    }

//    /**
//     * 유저 검색 - v1
//     */
//    @GetMapping("/search")
//    public ResponseEntity<CommonResponse<?>> readUserByQuery(@AuthenticationPrincipal AuthUser authUser, @PageableDefault Pageable pageable, @RequestParam(required = false) String domain, @RequestParam(required = false) String name, @RequestParam(required = false) LocalDateTime createdAt) {
//
//        Page<UserReadResponse> response = userService.readUserByQuery(authUser, pageable, domain, name, createdAt);
//
//        return ResponseEntity.ok(CommonResponse.success("유저 목록 조회 성공", response));
//    }

    /**
     * 유저 검색 v2 (In-memory Cache)
     */
    @GetMapping("/search_v2")
    public ResponseEntity<CommonResponse<?>> readUserByQueryInMemoryCache(@AuthenticationPrincipal AuthUser authUser, @PageableDefault Pageable pageable, @RequestParam(required = false) String domain, @RequestParam(required = false) String name, @RequestParam(required = false) LocalDateTime createdAt) {

        Page<UserReadResponse> response = userService.readUserByQueryInMemoryCache(authUser, pageable, domain, name, createdAt);

        return ResponseEntity.ok(CommonResponse.success("유저 목록 조회 성공", response));
    }

//    /**
//     * 유저 검색 - v3 (Redis 를 이용한 Remote Cache)
//     */
//    @GetMapping("/search_v3")
//    public ResponseEntity<CommonResponse<?>> readUserByQueryRedis(@AuthenticationPrincipal AuthUser authUser, @PageableDefault Pageable pageable, @RequestParam(required = false) String domain, @RequestParam(required = false) String name, @RequestParam(required = false) LocalDateTime createdAt) {
//
//        Page<UserReadResponse> response = userService.readUserByQueryRedis(authUser, pageable, domain, name, createdAt);
//
//        return ResponseEntity.ok(CommonResponse.success("유저 목록 조회 성공", response));
//    }
}