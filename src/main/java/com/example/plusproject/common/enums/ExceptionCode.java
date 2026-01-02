package com.example.plusproject.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    // 유저 회원가입시 email, phone 중복
    USER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."),
    USER_PHONE_DUPLICATE(HttpStatus.CONFLICT, "이미 사용중인 핸드폰 번호 입니다."),
    //유저 로그인 시 email 없음
    USER_NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "이메일이 없습니다"),
    USER_ALREADY_DELETED(HttpStatus.NOT_FOUND, "이미 탈퇴한 유저입니다"),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST,"패스워드가 일치하지 않습니다"),

    INVALID_TOKEN(HttpStatus.NOT_FOUND,"토큰이 없습니다"),
    MATCHES_PASSWORD(HttpStatus.BAD_REQUEST, "동일한 비밀번호로는 변경할 수 없습니다"),

    NO_PERMISSION(HttpStatus.FORBIDDEN,"작성자만 수정 및 삭제 가능합니다."),

    // 상품 조회
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),

    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "해당 주문건을 찾을 수 없습니다."),
    NOT_YOUR_ORDER(HttpStatus.FORBIDDEN, "본인의 주문이 아닙니다."),
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 주문은 본인의 주문이 아닙니다."),

    // 상품 존재 여부
    EXISTS_PRODUCT_NAME(HttpStatus.CONFLICT, "이미 사용중인 상품명 입니다."),
    OUT_OF_STOCK(HttpStatus.CONFLICT, "상품 재고가 부족합니다."),

    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),

    NO_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
