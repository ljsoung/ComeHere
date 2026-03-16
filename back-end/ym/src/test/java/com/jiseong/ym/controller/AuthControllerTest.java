package com.jiseong.ym.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 인증 API 테스트
 * POST /api/auth/signup
 * POST /api/auth/login
 */
class AuthControllerTest extends ControllerTestSupport {

    // ─── 회원가입 ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("회원가입 성공 → 201")
    void signUp_success() throws Exception {
        Map<String, Object> request = Map.of(
                "email", "user@test.com",
                "password", "password123",
                "nickname", "테스터",
                "university", "테스트대학교",
                "major", "컴퓨터공학과"
        );

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("이메일 중복 회원가입 → 409")
    void signUp_emailDuplicated() throws Exception {
        createUser("dup@test.com", "기존유저");

        Map<String, Object> request = Map.of(
                "email", "dup@test.com",
                "password", "password123",
                "nickname", "새유저",
                "university", "테스트대학교",
                "major", "컴퓨터공학과"
        );

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 사용 중인 이메일입니다."));
    }

    @Test
    @DisplayName("닉네임 중복 회원가입 → 409")
    void signUp_nicknameDuplicated() throws Exception {
        createUser("existing@test.com", "중복닉네임");

        Map<String, Object> request = Map.of(
                "email", "new@test.com",
                "password", "password123",
                "nickname", "중복닉네임",
                "university", "테스트대학교",
                "major", "컴퓨터공학과"
        );

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 사용 중인 닉네임입니다."));
    }

    @Test
    @DisplayName("이메일 형식 오류 → 400")
    void signUp_invalidEmail() throws Exception {
        Map<String, Object> request = Map.of(
                "email", "invalid-email",
                "password", "password123",
                "nickname", "테스터",
                "university", "테스트대학교",
                "major", "컴퓨터공학과"
        );

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 8자 미만 → 400")
    void signUp_shortPassword() throws Exception {
        Map<String, Object> request = Map.of(
                "email", "user@test.com",
                "password", "short",
                "nickname", "테스터",
                "university", "테스트대학교",
                "major", "컴퓨터공학과"
        );

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ─── 로그인 ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("로그인 성공 → 200, accessToken 반환")
    void login_success() throws Exception {
        createUser("login@test.com", "로그인유저");

        Map<String, Object> request = Map.of(
                "email", "login@test.com",
                "password", "password123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    @DisplayName("존재하지 않는 이메일 로그인 → 404")
    void login_userNotFound() throws Exception {
        Map<String, Object> request = Map.of(
                "email", "notfound@test.com",
                "password", "password123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("비밀번호 불일치 로그인 → 401")
    void login_wrongPassword() throws Exception {
        createUser("user@test.com", "유저");

        Map<String, Object> request = Map.of(
                "email", "user@test.com",
                "password", "wrongpassword"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("비밀번호가 올바르지 않습니다."));
    }
}