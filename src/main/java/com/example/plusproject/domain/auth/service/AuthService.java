package com.example.plusproject.domain.auth.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
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


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     *
     */
    @Transactional
    public AuthSignupResponse signUp(AuthSignupRequest authSignupRequest) {

        boolean emailChk = userRepository.existsByEmail(authSignupRequest.getEmail());
        boolean phoneChk = userRepository.existsByPhone(authSignupRequest.getPhone());

        if (emailChk) {
            throw new CustomException(ExceptionCode.USER_EMAIL_DUPLICATE);
        }

        if (phoneChk) {
            throw new CustomException(ExceptionCode.USER_PHONE_DUPLICATE);
        }

        User user = new User(
                authSignupRequest.getName(),
                authSignupRequest.getEmail(),
                passwordEncoder.encode(authSignupRequest.getPassword()),
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

        return AuthSignupResponse.from(user);
    }

    /**
     * 로그인
     */
    @Transactional
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {

        User user = userRepository.findByEmail(authLoginRequest.getEmail())
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND_EMAIL));

        if (user.isDeleted()) {
            throw new CustomException(ExceptionCode.USER_ALREADY_DELETED);
        }

        if (!passwordEncoder.matches(authLoginRequest.getPassword(), user.getPassword())) {
            throw new CustomException(ExceptionCode.PASSWORD_NOT_MATCH);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        return new AuthLoginResponse("Bearer " + token);
    }
}
