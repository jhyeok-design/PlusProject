package com.example.plusproject.domain.user.service;

import com.example.plusproject.common.config.RedisConfig;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.util.PasswordEncoder;
import com.example.plusproject.domain.search.entity.Search;
import com.example.plusproject.domain.search.repository.SearchRepository;
import com.example.plusproject.domain.search.service.SearchService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.plusproject.common.enums.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisConfig redisConfig;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SearchService searchService;
    private final SearchRepository searchRepository;
    private final String USERSEARCH_PREFIX = "userSearch:domain=%s:name=%s:page=%d:size=%d:createdAt=%s";
    /**
     * 유저 마이페이지 조회
     */
    @Transactional(readOnly = true)
    public UserReadResponse readMypage(AuthUser authUser) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        UserDto response = UserDto.from(user);

        return UserReadResponse.from(response);
    }

    /**
     * 유저 정보 수정
     */
    @Transactional
    public UserUpdateResponse updateUser(AuthUser authUser, UserUpdateRequest userUpdateRequest) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        boolean phoneExist = userRepository.existsByPhone(userUpdateRequest.getPhone());

        if (userUpdateRequest.getPassword() != null && passwordEncoder.matches(userUpdateRequest.getPassword(), user.getPassword())) {
            throw new CustomException(MATCHES_PASSWORD);
        }

        if (userUpdateRequest.getPhone() != null && phoneExist) {
            throw new CustomException(USER_PHONE_DUPLICATE);
        }

        user.update(userUpdateRequest);

        UserDto response = UserDto.from(user);

        return UserUpdateResponse.from(response);
    }

    /**
     * 유저 정보 삭제
     */
    @Transactional
    public void deleteUser(AuthUser authUser) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (user.isDeleted()) {
            throw new CustomException(USER_ALREADY_DELETED);
        }

        user.delete();
    }

    /**
     * 유저 검색 v1 - 아무 캐시도 사용하지 않음
     * */

    @Transactional(readOnly = true)
    public Page<UserReadResponse> readUserByQuery(AuthUser authUser,
                                                  Pageable pageable,
                                                  String domain,
                                                  String name,
                                                  LocalDateTime createdAt
    ) {
        // 1. 유저 id 조회 / 없으면 예외처리
        userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));

        // 2. 쿼리dsl 로 조회
        return userRepository.readUserByQuery(pageable, domain, name, createdAt);
    }

    /**
     * 유저 검색 v2 - cache 사용
     * */
    @Cacheable(
            cacheNames = "userSearch",
            key = "T(java.util.Objects).hash(#pageable, #domain, #name, #createdAt)"
    )
    @Transactional(readOnly = true)
    public Page<UserReadResponse> readUserByQueryInmemoryCache(AuthUser authUser,
                                                               Pageable pageable,
                                                               String domain,
                                                               String name,
                                                               LocalDateTime createdAt
    ) {
        // 1. 유저 id 조회 / 없으면 예외처리
        userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));

        // 2. 쿼리dsl 로 조회
        return userRepository.readUserByQuery(pageable, domain, name, createdAt);
    }

    /**
     * 유저 검색 v3 - Redis 사용
     * */
    @Transactional
    public Page<UserReadResponse> readUserByQueryRedis(AuthUser authUser,
                                                       Pageable pageable,
                                                       String domain,
                                                       String name,
                                                       LocalDateTime createdAt
    ) {
        // 1.cacheKey 생성
        // 2. redis get
        // 3. 있으면 => 역직렬화 => return
        // 4. 없으면 => DB 조회
        // 5. Redis set
        // 6. return

        //검색어 카운트 가져오기 위해 search 엔티티에서 가져옴
        searchRepository.findByKeyword(domain)
                .orElseGet(()->searchRepository.save(new Search(domain)));

        // 검색 기능 캐시키 생성
        String cacheKey = String.format(
                USERSEARCH_PREFIX,
                domain, name,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                createdAt != null ? createdAt.toString() : "null"
        );  //캐시 키 생성

        // 검색 기능 캐시 키 가져옴
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        //cache 데이터 있을경우

        if (cached != null) {

            log.info("Redis 감지");

            //cached List로 변환
            @SuppressWarnings("unchecked")  //괜찮아!
            List<UserReadResponse> content = (List<UserReadResponse>) cached;

            log.info("cached:{}",cached);

            return new PageImpl<>(content, pageable, content.size());

        }

        log.info("DB 조회");

        // 1. 유저 id 조회 / 없으면 예외처리
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(()->new CustomException(NOT_FOUND_USER));

        // 2. 쿼리dsl 로 조회
        Page<UserReadResponse> result = userRepository.readUserByQuery(pageable, domain, name, createdAt);

        // 3. Redis 저장
        redisTemplate.opsForValue().set(
                cacheKey,
                result.getContent(),
                Duration.ofMinutes(3)
        );

        // 4. 검색어 집계 + 처음 검색 시 테이블 생성
        searchService.recordSearch(domain);

        // 5. 리턴
        return result;

    }



}