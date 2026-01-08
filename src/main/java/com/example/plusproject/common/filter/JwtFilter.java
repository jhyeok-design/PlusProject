package com.example.plusproject.common.filter;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.enums.UserRole;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.util.JwtUtil;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(JwtUtil.AUTHORIZATION);

        if (header == null || !header.startsWith(JwtUtil.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = Objects.requireNonNull(header).substring(JwtUtil.BEARER_PREFIX.length());

        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(ExceptionCode.INVALID_TOKEN);
        }

        Long userId = jwtUtil.extractUserId(token);
        UserRole role = jwtUtil.extractUserRole(token);

        // 탈퇴유저 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
        if (user.isDeleted()) {
            throw new CustomException(ExceptionCode.USER_ALREADY_DELETED);
        }

        AuthUser authUser = new AuthUser(userId, role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("[JWT FILTER] 인증성공 userId:{}, userRole:{}", userId, role);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        return path.startsWith("/api/auth/");
    }
}
