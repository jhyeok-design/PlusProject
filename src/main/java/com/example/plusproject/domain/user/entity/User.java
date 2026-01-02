package com.example.plusproject.domain.user.entity;

import com.example.plusproject.common.entity.BaseEntity;
import com.example.plusproject.common.enums.UserRole;
import com.example.plusproject.domain.user.model.request.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_users_email",
                columnNames =  "email"
        ),
        @UniqueConstraint(
                name = "uk_users_phone",
                columnNames =  "phone"
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public User(
            String name,
            String email,
            String password,
            String nickname,
            String phone,
            String address
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.isDeleted = false;
        this.role = UserRole.USER;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(UserUpdateRequest request) {
        this.password = request.getPassword() != null ? request.getPassword() : this.password;
        this.name = request.getName() != null ? request.getName() : this.name;
        this.nickname = request.getNickname() != null ? request.getNickname() : this.nickname;
        this.phone = request.getPhone() != null ? request.getPhone() : this.phone;
        this.address = request.getAddress() != null ? request.getAddress() : this.address;
        updateModifiedAt();
    }
    public void updateModifiedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }

}
