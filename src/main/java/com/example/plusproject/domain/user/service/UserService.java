package com.example.plusproject.domain.user.service;

import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.common.util.PasswordEncoder;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.model.request.UserUpdateRequest;
import com.example.plusproject.domain.user.model.response.UserReadResponse;
import com.example.plusproject.domain.user.model.response.UserUpdateResponse;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.plusproject.common.enums.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CommonResponse<UserReadResponse> myPage(AuthUser authUser) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));

        return new CommonResponse<>(true,"마이페이지 조회 성공", UserReadResponse.from(user));
    }

    @Transactional
    public CommonResponse<UserUpdateResponse> infoUpdate(AuthUser authUser, UserUpdateRequest userUpdateRequest) {
        // 유저 확인
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));

        boolean phoneExist = userRepository.existsByPhone(userUpdateRequest.getPhone());


        // 유저 비밀번호 변경시 패스워드 인코딩
        if (userUpdateRequest.getPassword() != null &&
                passwordEncoder.matches(userUpdateRequest.getPassword(), user.getPassword())
        ) {
            throw new CustomException(MATCHES_PASSWORD);
        }
        // 핸드폰 번호 수정하려는시에 db 검증
        if (userUpdateRequest.getPhone() != null && phoneExist) {
            throw new CustomException(USER_PHONE_DUPLICATE);
        }


        user.update(userUpdateRequest);

        return new CommonResponse<>(true, "회원 정보 수정이 완료되었습니다", UserUpdateResponse.from(user));
    }

    @Transactional
    public CommonResponse<Void> deleteUser(AuthUser authUser) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));
        if (user.isDeleted()) {
            throw new CustomException(USER_ALREADY_DELETED);
        }

        user.delete();

        return new CommonResponse<>(true,"회원 탈퇴가 완료되었습니다", null);
    }
}
