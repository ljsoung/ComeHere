package com.jiseong.ym.dto.auth;

import lombok.Getter;

/**
 * 로그인 성공 응답 DTO
 * 프론트엔드는 이 토큰을 저장해두고 이후 요청 시 Authorization 헤더에 담아 전송한다.
 *
 * 응답 예시:
 * {
 *   "accessToken": "eyJhbGci...",
 *   "tokenType": "Bearer"
 * }
 */
@Getter
public class LoginResponse {

    private final String accessToken;
    private final String tokenType;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
        // 토큰 타입은 항상 Bearer로 고정
        this.tokenType = "Bearer";
    }
}