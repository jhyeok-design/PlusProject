package com.example.plusproject.domain.user.repository;

import com.example.plusproject.domain.user.model.response.UserReadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface UserCustomRepository {

    Page<UserReadResponse> readUserByQuery(Pageable pageable, String domain, String name, LocalDateTime createdAt);
}
