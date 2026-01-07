package com.example.plusproject.common.config;

import com.example.plusproject.common.filter.JwtFilter;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.common.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // csrf 빌활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 차단
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //인증, 인가 예외처리
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                        .accessDeniedHandler(jwtAccessDeniedHandler())
                )
                // 권한 설정
                .authorizeHttpRequests(auth -> auth
                    //auth 단은 전부 통과
                        .requestMatchers("/api/reviews/my-review").authenticated()
                        .requestMatchers("/api/auth/**").permitAll()
                        //상품, 게시판, 댓글의 GET 요청들은 전부 통과
                        .requestMatchers(HttpMethod.GET,
                                "/api/products/**",
                                "/api/reviews/**",
                                "/api/search/**",
                                "/api/posts/**"
                                )
                        .permitAll()
                    //상품에 대한 CUD 는 ADMIN 만 가능
                    .requestMatchers(("/api/products/**")).hasRole("ADMIN")
                        .requestMatchers("/api/orders/searchV1", "/api/orders/searchV2", "/api/orders/searchV3").permitAll()
                    //이 외 인증 필요
                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //인증 실패시 (401)
    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        ObjectMapper objectMapper = new ObjectMapper();

        ErrorResponse error = new ErrorResponse(401, "인증이 필요합니다.");

        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    objectMapper.writeValueAsString(error)
            );
        };
    }

    //인가 실패시 (403)
    @Bean
    public AccessDeniedHandler jwtAccessDeniedHandler() {
        ObjectMapper objectMapper = new ObjectMapper();

        ErrorResponse error = new ErrorResponse(403, "인가가 필요합니다.");
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    objectMapper.writeValueAsString(error)
            );
        };
    }
}
