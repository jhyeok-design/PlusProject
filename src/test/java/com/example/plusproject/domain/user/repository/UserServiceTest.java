package com.example.plusproject.domain.user.repository;
import com.example.plusproject.common.enums.UserRole;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.filter.JwtFilter;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.util.JwtUtil;
import com.example.plusproject.common.util.PasswordEncoder;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.model.request.UserUpdateRequest;
import com.example.plusproject.domain.user.model.response.UserReadResponse;
import com.example.plusproject.domain.user.model.response.UserUpdateResponse;
import com.example.plusproject.domain.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtUtil jwtUtil;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }



    private final AuthUser authUser = new AuthUser(1L, UserRole.ADMIN);

    private final User user = new User("test",
            "test@test.com",
            "password",
            "nicknametest",
            "010-0000-0001",
            "testsaddress"
    );

    @Test
    @DisplayName("마이페이지 조회 성공")
    void readMypage_success() {
        // given
//        String name,
//        String email,
//        String password,
//        String nickname,
//        String phone,
//        String address
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // when
        UserReadResponse response = userService.readMypage(authUser);

        // then
        assertThat(response.getName()).isEqualTo("test");
        assertThat(response.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("마이페이지 조회 실패 - 유저 없음")
    void readMypage_fail() {
        // given
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.readMypage(authUser))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("유저 정보 수정 성공")
    void updateUser_success() {
        // given

        UserUpdateRequest request = new UserUpdateRequest("test2","test2password","test2","010-0000-0002","changeaddress");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByPhone(request.getPhone())).thenReturn(false);

        // when
        UserUpdateResponse response = userService.updateUser(authUser, request);

        // then
        assertThat(response.getPhone()).isEqualTo("010-0000-0002");
        assertThat(response.getAddress()).isEqualTo("changeaddress");
    }

    @Test
    @DisplayName("유저 정보 수정 실패 - 기존 비밀번호와 동일")
    void updateUser_fail_password() {
        // given
        AuthUser authUser = new AuthUser(1L, UserRole.USER);

        UserUpdateRequest request = new UserUpdateRequest(null,"password",null,null,null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(authUser, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("동일한 비밀번호로는 변경할 수 없습니다");
    }

    @Test
    @DisplayName("유저 삭제 성공")
    void deleteUser_success() {
        // given

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // when
        userService.deleteUser(authUser);

        // then
        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("유저 삭제 실패 - 이미 삭제됨")
    void deleteUser_fail_alreadyDeleted() {
        // given
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        user.delete();
        // when & then
        assertThatThrownBy(() -> userService.deleteUser(authUser))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("이미 탈퇴한 유저입니다");
    }

    @Test
    @DisplayName("JWT 토큰이 유효하면 SecurityContext에 인증 정보 저장")
    void doFilter_validToken_success() throws Exception {
        // given
        String token = "valid.jwt.token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn(1L);
        when(jwtUtil.extractUserRole(token)).thenReturn(UserRole.ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // when
        jwtFilter.doFilter(request, response, filterChain);

        // then
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication).isNotNull();
    }


}
