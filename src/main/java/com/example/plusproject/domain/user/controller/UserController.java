package com.example.plusproject.domain.user.controller;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.search.service.SearchService;
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
    private final SearchService searchService;
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
     * 유저 검색 v1
     * */
    @GetMapping("/search-v1")
    public ResponseEntity<CommonResponse<?>> readUserByQuery(@AuthenticationPrincipal AuthUser authUser,
                                                             @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                             @RequestParam(required = false) String domain,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false)LocalDateTime createdAt
    ) {

        // 검색어 집계
        searchService.recordSearch(domain);

        return ResponseEntity.ok(CommonResponse.success(
                "유저 목록 조회 성공",
                userService.readUserByQuery(authUser, pageable, domain, name, createdAt)
        ));
    }

    /**
     * 유저 검색 v2 - 캐시 사용
     * */
    @GetMapping("/search-v2")
    public ResponseEntity<CommonResponse<?>> readUserByQueryInmemoryCache(@AuthenticationPrincipal AuthUser authUser,
                                                             @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                             @RequestParam(required = false) String domain,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false)LocalDateTime createdAt
                                                             ) {

        // 검색어 집계
        searchService.recordSearch(domain);

        return ResponseEntity.ok(CommonResponse.success(
                "유저 목록 조회 성공",
                userService.readUserByQueryInmemoryCache(authUser, pageable, domain, name, createdAt)
        ));
    }

    /**
     * 유저 검색 v3 - 캐시 사용
     * */
    @GetMapping("/search-v3")
    public ResponseEntity<CommonResponse<?>> readUserByQueryRedis(@AuthenticationPrincipal AuthUser authUser,
                                                             @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                             @RequestParam(required = false) String domain,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false)LocalDateTime createdAt
    ) {


        return ResponseEntity.ok(CommonResponse.success(
                "유저 목록 조회 성공",
                userService.readUserByQueryRedis(authUser, pageable, domain, name, createdAt)
        ));
    }


}
