package com.example.plusproject.domain.product.repository;

import com.example.plusproject.domain.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
class ProductBatchRepositoryTest {

    @Autowired
    private ProductBatchRepository productBatchRepository;

    private static final int TOTAL_PRODUCTS = 5_000_000;
    private static final int BATCH_SIZE = 5_000;

    private static final String[] PREFIXES = {
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

    private static final String[] PRODUCT_NAMES = {
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

    @Test
    void bulkInsert_Products_Test() {

        System.out.println("상품 생성 시작");

        Random random = new Random();
        int count = 0;

        for (int batchStart = 0; batchStart < TOTAL_PRODUCTS; batchStart += BATCH_SIZE) {
            int batchEnd = Math.min(batchStart + BATCH_SIZE, TOTAL_PRODUCTS);
            List<Product> batch = new ArrayList<>(batchEnd - batchStart);

            for (int i = batchStart; i < batchEnd; i++) {

                String prefix = PREFIXES[random.nextInt(PREFIXES.length)];
                String baseName = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)];
                String name = prefix + " " + baseName + "_" + i;

                Long price = randomPrice(random);
                String description = "상품 설명";
                Long quantity = 500L;

                batch.add(new Product(name, price, description, quantity));
                count++;
            }

            productBatchRepository.batchInsert(batch);
            System.out.println("Insert Batch Products: " + count);
        }

        System.out.println("상품 500만개 생성 완료");
    }

    private long randomPrice(Random random) {
        int type = random.nextInt(3);

        return switch (type) {
            case 0 -> 5_000 + random.nextInt(5_000);
            case 1 -> 10_000 + random.nextInt(20_000);
            default -> 30_000 + random.nextInt(50_000);
        };
    }
}
