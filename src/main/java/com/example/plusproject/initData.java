package com.example.plusproject;

import com.example.plusproject.common.enums.UserRole;
import com.example.plusproject.domain.post.entity.Post;
import com.example.plusproject.domain.post.repository.PostRepository;
import com.example.plusproject.common.util.PasswordEncoder;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class initData {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;

//    @PostConstruct
//    @Transactional
//    public void init(){
//        User admin = new User("관리자",
//                "admin@example.com",
//                passwordEncoder.encode("admin123"),
//                "admin",
//                "01012345678",
//                "서울특별시 중구",
//                UserRole.ADMIN);
//
//        userRepository.save(admin);
//
//
//        Post post1 =new Post("제목", "내용", admin);
//        postRepository.save(post1);
//
//    }
}
