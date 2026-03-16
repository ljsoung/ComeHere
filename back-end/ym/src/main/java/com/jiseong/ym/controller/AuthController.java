package com.jiseong.ym.controller;

import com.jiseong.ym.dto.auth.LoginRequest;
import com.jiseong.ym.dto.auth.LoginResponse;
import com.jiseong.ym.dto.auth.SignUpRequest;
import com.jiseong.ym.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원가입 / 로그인 API 컨트롤러
 * SecurityConfig에서 /api/auth/** 는 인증 없이 접근 가능하도록 설정되어 있다.
 */
@Tag(name = "인증", description = "회원가입 / 로그인")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/signup
     * 회원가입 처리. 성공 시 201 Created 반환.
     * @Valid 로 SignUpRequest 필드 검증 → 실패 시 GlobalExceptionHandler에서 400 반환
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * POST /api/auth/login
     * 로그인 처리. 성공 시 JWT 토큰을 담은 LoginResponse 반환.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}