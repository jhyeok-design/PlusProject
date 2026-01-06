package com.example.plusproject.domain.user.repository;

import com.example.plusproject.common.enums.UserRole;
import com.example.plusproject.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class UserBatchRepositoryTest {
    @Autowired
    private UserBatchRepository userBatchRepository;

    private static final int TOTAL_USERS = 5_000_000;
    private static final int BATCH_SIZE = 1_000;

    private static final String[] LAST_NAMES = {
            "김", "이", "박", "최", "정",
            "강", "조", "윤", "장", "임",
            "한", "오", "서", "신", "권",
            "황", "안", "송", "전", "홍",
            "유", "고", "문", "양", "손",
            "배", "백", "허", "남", "심"
    };
    private static final String[] FIRST_NAME_1 = {
            "민", "서", "지", "현", "우",
            "수", "영", "준", "태", "하",
            "도", "윤", "예", "은", "재",
            "성", "경", "주", "혁", "진"
    };
    private static final String[] FIRST_NAME_2 = {
            "수", "빈", "훈", "석", "진",
            "아", "림", "호", "윤", "원",
            "영", "민", "현", "은", "경",
            "우", "재", "연", "준", "하"
    };

    private static final String[] NICKNAME_PREFIX = {
            "졸린", "배고픈", "늦잠잔", "커피중독", "집에가고픈",
            "멍때리는", "생각많은", "현타온", "의욕없는", "월요병",
            "느긋한", "조용한", "성실한", "단단한", "담백한",
            "솔직한", "차분한", "담대한", "꾸준한", "신중한",
            "버그잡는", "커밋하는", "리팩토링중인", "빌드깨진",
            "로그보는", "배포중인", "테스트하는", "코드짜는",
            "쿼리날리는", "서버지키는"
    };


    private static final String[] EMAIL_DOMAINS = {
            "gmail.com",
            "naver.com",
            "daum.net",
            "kakao.com",
            "hanmail.net",
            "nate.com",
            "icloud.com",
            "outlook.com",
            "hotmail.com",
            "live.com",
            "yahoo.com",
            "yahoo.co.kr",
            "gmail.com",
            "protonmail.com",
            "proton.me",
    };
    @Autowired
    private UserRepository userRepository;


    @Test
    void bulkInsert_Users_50K_test() {

        System.out.println("5만건 생성 시작");

        Random random = new Random();
        List<User> batch = new ArrayList<>(BATCH_SIZE);
        String runId = String.format("%02d", (System.currentTimeMillis() / 1000) % 100);

        for (int i = 0; i< TOTAL_USERS; i++ ) {

            String name = LAST_NAMES[random.nextInt(LAST_NAMES.length)]+
                    FIRST_NAME_1[random.nextInt(FIRST_NAME_1.length)]+
                    FIRST_NAME_2[random.nextInt(FIRST_NAME_2.length)];

            String nickname = NICKNAME_PREFIX[random.nextInt(NICKNAME_PREFIX.length)] +
                    name;

            String phone =
                    "010-" +
                            String.format("%04d", Integer.parseInt(runId) * 100 + (i / 1000)) + "-" +
                            String.format("%04d", i % 10000);

            String email =
                    "user_bulk"+runId+"_"+i
                            +"@"+EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];

            User user = new User(
                    name,
                    email,
                    "password",
                    nickname,
                    phone,
                    "서울시 주소지 어딘가"+i
            );

            batch.add(user);

            if (batch.size() == BATCH_SIZE) {
                userBatchRepository.batchInsert(batch);
                batch.clear();
                System.out.println("[ Inserted ]"+(i+1));
            }
        }

        if (!batch.isEmpty()) {
            userBatchRepository.batchInsert(batch);
        }

        User admin = new User(
                "admin",
                "admin@example.com",
                "password",
                "admin",
                "010-9999-9999",
                "어드민 주소지",
                UserRole.ADMIN
        );
        //ADMIN 없을 시 어드민계정 생성
        if (!userRepository.existsByEmail("admin@example.com")) {
            userBatchRepository.adminInsert(admin);
        }
        System.out.println("유저 5만명 생성 완료");
    }
}
