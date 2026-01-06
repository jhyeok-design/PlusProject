package com.example.plusproject.domain.review.service;

import com.example.plusproject.domain.review.model.ReviewBatchDto;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ReviewBulkInsertService {

    private final JdbcTemplate jdbcTemplate;

    Faker faker = new Faker(new Locale("ko"));

    private static final String[] prefix = {
            "작은", "거대한", "신비한", "고대의", "전설의",
            "빛나는", "어두운", "희귀한", "평범한", "미지의",
            "강화된", "각성한", "봉인된", "부서진", "정제된",
            "불안정한", "완벽한", "변이된", "진화한", "오염된",
            "심연의", "천공의", "차원의", "시공의", "혼돈의",
            "질서의", "파멸의", "창조의", "재앙의", "성스러운",
            "분노한", "슬픈", "광기의", "침묵의", "속삭이는",
            "울부짖는", "잊혀진", "금지된", "저주받은", "축복받은",
            "하급", "중급", "상급", "최상급", "궁극의",
            "초월한", "전승된", "전설급", "신화급", "고유한"
    };

    private static final String[] stoneKeywords = {
            "돌맹이", "조약돌", "자갈", "암석", "암반", "원석", "광석",
            "화강암", "현무암", "석회암", "사암", "대리석", "편마암",
            "금돌", "은돌", "동돌", "철돌", "강철석",
            "루비석", "사파이어석", "에메랄드석", "다이아석", "오팔석",
            "토파즈석", "자수정석", "가넷석", "진주석",
            "불의돌", "물의돌", "바람의돌", "대지의돌", "번개의돌",
            "얼음의돌", "빛의돌", "어둠의돌", "독의돌", "용암석",
            "마법돌", "고대의돌", "전설의돌", "신성석", "저주의돌",
            "룬석", "마나석", "소울스톤", "에테르석",
            "드래곤스톤", "피닉스스톤", "타이탄석",
            "별의돌", "혜성석", "운석", "성운석", "초신성석",
            "블랙홀석", "코스믹스톤", "아스트랄석", "차원석",
            "행운의돌", "희망석", "절망석", "시간의돌", "기억의돌",
            "운명의돌", "각성석", "봉인석", "재생석", "균열석"
    };

    public String bulkInsert() {

        long start = System.currentTimeMillis();

        int totalReview = 5_000_000;
        int batchSize = 5_000; // 한 번에 처리할 단위

        for (int i = 0; i < totalReview / batchSize; i++) {
            List<ReviewBatchDto> reviews = new ArrayList<>();

            for (int j = 0; j < batchSize; j++) {

                String content = "훌륭한 " + faker.options().nextElement(prefix)
                        + " " + faker.options().nextElement(stoneKeywords) + "이네요.";

                int score = faker.number().numberBetween(1, 6); // 마지막 숫자 미포함 (1 ~ 5)

                Long randomUserId = faker.number().numberBetween(1L, 3L);
                Long randomProductId = faker.number().numberBetween(1L, 3L);
                LocalDateTime createdAt = LocalDateTime.now();
                LocalDateTime updatedAt = LocalDateTime.now();

                reviews.add(new ReviewBatchDto(randomUserId, randomProductId, content, score, createdAt, updatedAt));
            }

            String sql = "insert into reviews (user_id, product_id, content, score, created_at, updated_at) values (?, ?, ?, ?, ?, ?)";

            jdbcTemplate.batchUpdate(sql, reviews, 5_000, (ps, review) -> {
                ps.setLong(1, review.getUserId());
                ps.setLong(2, review.getProductId());
                ps.setString(3, review.getContent());
                ps.setInt(4, review.getScore());
                ps.setTimestamp(5, Timestamp.valueOf(review.getCreatedAt()));
                ps.setTimestamp(6, Timestamp.valueOf(review.getUpdatedAt()));
            });
        }

        long end = System.currentTimeMillis();
        long totalSecond = (end - start) / 1000;

        return (totalSecond / 60) + "분 " + (totalSecond % 60) + "초";
    }
}
