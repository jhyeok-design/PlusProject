package com.example.plusproject.domain.user.service;

import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.util.PasswordEncoder;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.model.UserDto;
import com.example.plusproject.domain.user.model.request.UserUpdateRequest;
import com.example.plusproject.domain.user.model.response.UserReadResponse;
import com.example.plusproject.domain.user.model.response.UserUpdateResponse;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import static com.example.plusproject.common.enums.ExceptionCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 마이페이지 조회
     * */
    @Transactional(readOnly = true)
    public UserReadResponse readMypage(AuthUser authUser) {

        // 1. 유저 id 조회 / 없으면 예외처리
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));

        // 2. userDto 에 담음
        UserDto response = UserDto.from(user);

        // 3. DTO 리턴
        return UserReadResponse.from(response);
    }

    /**
     * 유저 정보 수정
     * */
    @Transactional
    public UserUpdateResponse updateUser(AuthUser authUser, UserUpdateRequest userUpdateRequest) {

        // 1. 유저 id 조회 / 없으면 예외처리
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));

        // 2. 정보 수정시 핸드폰 번호 중복 조회
        boolean phoneExist = userRepository.existsByPhone(userUpdateRequest.getPhone());


        // 3. 유저 비밀번호 변경시 패스워드 인코딩
        if (userUpdateRequest.getPassword() != null &&
                passwordEncoder.matches(userUpdateRequest.getPassword(), user.getPassword())
        ) {
            throw new CustomException(MATCHES_PASSWORD);
        }
        // 4. 핸드폰 번호 수정하려는시에 db 검증
        if (userUpdateRequest.getPhone() != null && phoneExist) {
            throw new CustomException(USER_PHONE_DUPLICATE);
        }

        // 5. 수정 정보 업데이트
        user.update(userUpdateRequest);

        // 6. userDto 담음
        UserDto response = UserDto.from(user);

        // 6. DTO 리턴
        return UserUpdateResponse.from(response);
    }

    /**
     * 유저 정보 삭제
     * */
    @Transactional
    public void deleteUser(AuthUser authUser) {

        // 1. 유저 id 조회 / 없으면 예외처리

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));

        // 2. 이미 소프트 딜리트 된 유저일 경우 예외처리
        if (user.isDeleted()) {
            throw new CustomException(USER_ALREADY_DELETED);
        }

        // 3. 유저 삭제
        user.delete();
    }

    /**
     * 유저 검색
     * */
    @Cacheable(
            cacheNames = "userSearch",
            key = "T(java.util.Objects).hash(#pageable, #domain, #name, #createdAt)"
    )
    @Transactional(readOnly = true)
    public Page<UserReadResponse> readUserByQuery(AuthUser authUser,
                                Pageable pageable,
                                String domain,
                                String name,
                                LocalDateTime createdAt
    ) {
        // 1. 유저 id 조회 / 없으면 예외처리
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));

        // 2. 쿼리dsl 로 조회
        return userRepository.readUserByQuery(pageable, domain, name, createdAt);
    }

}
