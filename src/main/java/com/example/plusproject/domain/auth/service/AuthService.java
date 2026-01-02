package com.example.plusproject.domain.auth.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.common.util.JwtUtil;
import com.example.plusproject.common.util.PasswordEncoder;
import com.example.plusproject.domain.auth.model.request.AuthLoginRequest;
import com.example.plusproject.domain.auth.model.request.AuthSignupRequest;
import com.example.plusproject.domain.auth.model.response.AuthLoginResponse;
import com.example.plusproject.domain.auth.model.response.AuthSignupResponse;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommonResponse<AuthSignupResponse> signUp(AuthSignupRequest authSignupRequest) {
        //email, phone 유니크인지 확인
        boolean emailChk = userRepository.existsByEmail(authSignupRequest.getEmail());
        boolean phoneChk = userRepository.existsByPhone(authSignupRequest.getPhone());

        if (emailChk) {
            throw new CustomException(ExceptionCode.USER_EMAIL_DUPLICATE);
        }

        if (phoneChk) {
            throw new CustomException(ExceptionCode.USER_PHONE_DUPLICATE);
        }

        //비밀번호 암호화
        log.info("beforeEncode:{}", authSignupRequest.getPassword());
        String encodePassword = passwordEncoder.encode(authSignupRequest.getPassword());
        log.info("encodePassword:{}",encodePassword);
        //유저 엔티티에 새 유저 생성
        User user = new User(
                authSignupRequest.getName(),
                authSignupRequest.getEmail(),
                encodePassword,//암호화된 비밀번호
                authSignupRequest.getNickname(),
                authSignupRequest.getPhone(),
                authSignupRequest.getAddress()
        );
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("uk_users_email")) {
                throw new CustomException(ExceptionCode.USER_EMAIL_DUPLICATE);
            }
            if (e.getMessage().contains("uk_users_phone")) {
                throw new CustomException(ExceptionCode.USER_PHONE_DUPLICATE);
            }
            throw e;
        }

        //리턴
        return new CommonResponse<>(true,"회원가입이 완료되었습니다.",AuthSignupResponse.from(user));
    }

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {
        //이메일 검증
        User user = userRepository.findByEmail(authLoginRequest.getEmail())
                .orElseThrow(()-> new CustomException(ExceptionCode.USER_NOT_FOUND_EMAIL));
        //isDeleted 체크
        if (user.isDeleted()) {
            throw new CustomException(ExceptionCode.USER_ALREADY_DELETED);
        }

        //비밀번호 검증
        if (!passwordEncoder.matches(authLoginRequest.getPassword(),user.getPassword())) {
            throw new CustomException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        //토큰 발급
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        //리턴
        return new AuthLoginResponse("Bearer "+token);
    }
}
