package com.example.plusproject.domain.user.repository;

import com.example.plusproject.common.util.PasswordEncoder;
import com.example.plusproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserBatchRepository {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public void batchInsert(List<User> users) {

        String sql = """
                    INSERT INTO users
                    (name, email, password, nickname, phone, address,
                     role, is_deleted, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, users, users.size(),
                ((ps, user) -> {
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, passwordEncoder.encode(user.getPassword()));
                    ps.setString(4, user.getNickname());
                    ps.setString(5, user.getPhone());
                    ps.setString(6, user.getAddress());
                    ps.setString(7, user.getRole().name());
                    ps.setBoolean(8, false);
                    ps.setTimestamp(9, Timestamp.valueOf(user.getCreatedAt()));
                    ps.setTimestamp(10, Timestamp.valueOf(user.getUpdatedAt()));
                })
        );
    }

    public void adminInsert(User user) {

        String sql = """
                    INSERT INTO users
                    (name, email, password, nickname, phone, address,
                     role, is_deleted, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql, user.getName(), user.getEmail(), passwordEncoder.encode(user.getPassword()),
                user.getNickname(), user.getPhone(), user.getAddress(), user.getRole().name(), false,
                Timestamp.valueOf(user.getCreatedAt()), Timestamp.valueOf(user.getUpdatedAt())
        );
    }
}
