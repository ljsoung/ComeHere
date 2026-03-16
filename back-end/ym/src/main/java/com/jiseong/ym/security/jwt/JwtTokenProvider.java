package com.jiseong.ym.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // application-local.properties 의 jwt.secret 값을 주입
    @Value("${jwt.secret}")
    private String secret;

    // application.properties 의 jwt.expiration 값을 주입 (기본값: 86400000ms = 24시간)
    @Value("${jwt.expiration}")
    private long expiration;

    // secret 문자열로 HMAC-SHA 서명 키 생성
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 이메일을 subject로 담은 JWT 토큰 생성
     * 로그인 성공 시 호출됨
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)                                                         // 토큰 주체 (이메일)
                .issuedAt(new Date())                                                   // 발급 시각
                .expiration(new Date(System.currentTimeMillis() + expiration))          // 만료 시각
                .signWith(getSigningKey())                                              // 서명
                .compact();
    }

    /**
     * 토큰에서 이메일(subject) 추출
     * 필터에서 인증 정보를 복원할 때 사용
     */
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 토큰 유효성 검사
     * 서명 불일치, 만료, 형식 오류 등이 있으면 false 반환
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰을 파싱해서 Claims(payload) 반환
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}