package com.jiseong.ym.controller;

import com.jiseong.ym.entity.Application;
import com.jiseong.ym.entity.Bookmark;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 사용자 API 테스트
 * GET    /api/users/me
 * PATCH  /api/users/me
 * GET    /api/users/me/posts
 * GET    /api/users/me/applications
 * GET    /api/users/me/bookmarks
 */
class UserControllerTest extends ControllerTestSupport {

    private User user;
    private User other;

    @BeforeEach
    void setUp() {
        user  = createUser("user@test.com", "유저");
        other = createUser("other@test.com", "타인");
    }

    // ─── 내 프로필 조회 ──────────────────────────────────────────────────────

    @Test
    @DisplayName("내 프로필 조회 → 200")
    void getMyProfile_success() throws Exception {
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", bearerToken(user.getEmail())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.nickname").value("유저"))
                .andExpect(jsonPath("$.university").value("테스트대학교"));
    }

    @Test
    @DisplayName("비인증 프로필 조회 → 4xx")
    void getMyProfile_unauthorized() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().is4xxClientError());
    }

    // ─── 내 프로필 수정 ──────────────────────────────────────────────────────

    @Test
    @DisplayName("내 프로필 수정 → 200")
    void updateMyProfile_success() throws Exception {
        Map<String, Object> request = Map.of(
                "nickname", "새닉네임",
                "university", "새대학교",
                "major", "소프트웨어학과",
                "bio", "안녕하세요",
                "githubUrl", "https://github.com/test",
                "portfolioUrl", ""
        );

        mockMvc.perform(patch("/api/users/me")
                        .header("Authorization", bearerToken(user.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("새닉네임"))
                .andExpect(jsonPath("$.bio").value("안녕하세요"));
    }

    @Test
    @DisplayName("이미 사용중인 닉네임으로 수정 → 409")
    void updateMyProfile_nicknameDuplicated() throws Exception {
        // other 유저가 이미 "타인" 닉네임 사용 중
        Map<String, Object> request = Map.of(
                "nickname", "타인", // other 유저의 닉네임
                "university", "테스트대학교",
                "major", "컴퓨터공학과"
        );

        mockMvc.perform(patch("/api/users/me")
                        .header("Authorization", bearerToken(user.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 사용 중인 닉네임입니다."));
    }

    // ─── 마이페이지 ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("내가 작성한 모집글 목록 → 200, 페이징")
    void getMyPosts_success() throws Exception {
        createPost(user); // 내 글
        createPost(other); // 타인 글 (포함되면 안 됨)

        mockMvc.perform(get("/api/users/me/posts")
                        .header("Authorization", bearerToken(user.getEmail())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1)); // 내 글만
    }

    @Test
    @DisplayName("내가 지원한 모집글 목록 → 200, 페이징")
    void getMyApplications_success() throws Exception {
        RecruitmentPost post = createPost(other);
        applicationRepository.save(Application.builder()
                .applicant(user)
                .post(post)
                .build());

        mockMvc.perform(get("/api/users/me/applications")
                        .header("Authorization", bearerToken(user.getEmail())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("내가 북마크한 모집글 목록 → 200, 페이징")
    void getMyBookmarks_success() throws Exception {
        RecruitmentPost post = createPost(other);
        bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .post(post)
                .build());

        mockMvc.perform(get("/api/users/me/bookmarks")
                        .header("Authorization", bearerToken(user.getEmail())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("페이지 파라미터 적용 확인 → 200")
    void getMyPosts_pagination() throws Exception {
        // 글 3개 작성
        createPost(user);
        createPost(user);
        createPost(user);

        mockMvc.perform(get("/api/users/me/posts")
                        .header("Authorization", bearerToken(user.getEmail()))
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content.length()").value(2)) // 한 페이지에 2개
                .andExpect(jsonPath("$.totalPages").value(2));
    }
}