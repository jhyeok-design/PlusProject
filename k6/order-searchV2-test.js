import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    // 1. 부하 단계 설정 (점진적 증가 및 피크 테스트)
    stages: [
        { duration: '10s', target: 50 },   // 워밍업: 50명까지 점진적 증가
        { duration: '20s', target: 100 },  // 정상 부하: 100명 유지
        { duration: '20s', target: 200 },  // 피크 부하: 200명까지 폭격
        { duration: '10s', target: 0 },    // 점진적 종료
    ],

    // 2. 합격/불합격 기준 설정
    thresholds: {
        http_req_failed: ['rate<0.01'],    // 에러율 1% 미만 유지
        http_req_duration: ['p(95)<500'],  // 95%의 요청은 500ms 이내 응답 (ms 단위!)
    },
};

export default function () {
    // 검색어 인코딩
    const keyword = encodeURIComponent('천공의 루비석_31166');

    // API URL (페이지네이션 포함)
    const url = `http://host.docker.internal:8080/api/orders/searchV2?keyword=${keyword}&page=0&size=10`;

    // const params = {
    //     headers: {
    //         'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMSIsInVzZXJSb2xlIjoiQURNSU4iLCJpYXQiOjE3Njc3NzA4OTQsImV4cCI6MTc2Nzc3NDQ5NH0.svM-kQKCm8oiBR9tXXObRyc30U4zoc6X76hyhRPfYxg',
    //         'Content-Type': 'application/json',
    //     },
    // };

    // GET 요청 발사
    const res = http.get(url);

    // 3. 응답 결과 검증
    check(res, {
        '응답 코드가 200인가': (r) => r.status === 200,
        '데이터가 비어있지 않은가': (r) => r.body.length > 0,
    });

    // 4. 짧은 휴식 (사용자 행동 모사 및 서버 과부하 방지)
    // sleep(0.1) = 100ms 쉬기
    sleep(0.1);
}