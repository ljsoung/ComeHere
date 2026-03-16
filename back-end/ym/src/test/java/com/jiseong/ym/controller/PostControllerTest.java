package com.jiseong.ym.controller;

import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 모집글 API 테스트
 * POST   /api/posts
 * GET    /api/posts
 * GET    /api/posts/{postId}
 * PATCH  /api/posts/{postId}
 * DELETE /api/posts/{postId}
 * PATCH  /api/posts/{postId}/status
 */
class PostControllerTest extends ControllerTestSupport {

    private User author;
    private User other;
    private RecruitmentPost post;

    @BeforeEach
    void setUp() {
        author = createUser("author@test.com", "작성자");
        other  = createUser("other@test.com", "타인");
        post   = createPost(author);
    }

    // ─── 모집글 작성 ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("모집글 작성 성공 → 201")
    void createPost_success() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "프론트엔드 팀원 구합니다",
                "content", "React 할 줄 아시는 분",
                "category", "STUDY",
                "recruitmentCount", 2,
                "period", "2개월",
                "deadline", LocalDate.now().plusDays(10).toString(),
                "meetingType", "ONLINE",
                "stacks", List.of("React", "TypeScript")
        );

        mockMvc.perform(post("/api/posts")
                        .header("Authorization", bearerToken(author.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("프론트엔드 팀원 구합니다"))
                .andExpect(jsonPath("$.stacks").isArray());
    }

    @Test
    @DisplayName("비인증 모집글 작성 → 401 또는 403")
    void createPost_unauthorized() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "제목", "content", "내용",
                "category", "STUDY", "recruitmentCount", 1,
                "period", "1개월", "deadline", LocalDate.now().plusDays(5).toString(),
                "meetingType", "ONLINE"
        );

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("마감일이 과거인 경우 → 400")
    void createPost_invalidDeadline() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "제목", "content", "내용",
                "category", "STUDY", "recruitmentCount", 1,
                "period", "1개월",
                "deadline", LocalDate.now().minusDays(1).toString(), // 과거 날짜
                "meetingType", "ONLINE"
        );

        mockMvc.perform(post("/api/posts")
                        .header("Authorization", bearerToken(author.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ─── 모집글 목록 조회 ────────────────────────────────────────────────────

    @Test
    @DisplayName("모집글 목록 조회 (비회원 가능) → 200")
    void getPosts_success() throws Exception {
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("키워드 검색 → 200, 일치하는 결과 반환")
    void getPosts_keywordSearch() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("keyword", "백엔드"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("백엔드 팀원 모집"));
    }

    @Test
    @DisplayName("카테고리 필터 → 200")
    void getPosts_categoryFilter() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("category", "STUDY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("매칭되지 않는 키워드 검색 → 200, 빈 결과")
    void getPosts_noResult() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("keyword", "존재하지않는키워드"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // ─── 모집글 상세 조회 ────────────────────────────────────────────────────

    @Test
    @DisplayName("모집글 상세 조회 → 200, 조회수 증가")
    void getPost_success() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("백엔드 팀원 모집"))
                .andExpect(jsonPath("$.viewCount").value(1)); // 조회 시 1 증가
    }

    @Test
    @DisplayName("존재하지 않는 모집글 조회 → 404")
    void getPost_notFound() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("모집글을 찾을 수 없습니다."));
    }

    // ─── 모집글 수정 ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("작성자 모집글 수정 → 200")
    void updatePost_success() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "수정된 제목",
                "content", "수정된 내용",
                "category", "CONTEST",
                "recruitmentCount", 5,
                "period", "6개월",
                "deadline", LocalDate.now().plusDays(30).toString(),
                "meetingType", "HYBRID"
        );

        mockMvc.perform(patch("/api/posts/{postId}", post.getId())
                        .header("Authorization", bearerToken(author.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.category").value("CONTEST"));
    }

    @Test
    @DisplayName("타인이 모집글 수정 → 403")
    void updatePost_forbidden() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "타인이 수정", "content", "내용",
                "category", "STUDY", "recruitmentCount", 1,
                "period", "1개월", "deadline", LocalDate.now().plusDays(5).toString(),
                "meetingType", "ONLINE"
        );

        mockMvc.perform(patch("/api/posts/{postId}", post.getId())
                        .header("Authorization", bearerToken(other.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("모집글 작성자만 수행할 수 있습니다."));
    }

    // ─── 모집글 삭제 ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("작성자 모집글 삭제 → 204")
    void deletePost_success() throws Exception {
        mockMvc.perform(delete("/api/posts/{postId}", post.getId())
                        .header("Authorization", bearerToken(author.getEmail())))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("타인이 모집글 삭제 → 403")
    void deletePost_forbidden() throws Exception {
        mockMvc.perform(delete("/api/posts/{postId}", post.getId())
                        .header("Authorization", bearerToken(other.getEmail())))
                .andExpect(status().isForbidden());
    }

    // ─── 모집 상태 변경 ──────────────────────────────────────────────────────

    @Test
    @DisplayName("작성자 모집 상태 변경 (OPEN → CLOSED) → 200")
    void changeStatus_success() throws Exception {
        Map<String, Object> request = Map.of("status", "CLOSED");

        mockMvc.perform(patch("/api/posts/{postId}/status", post.getId())
                        .header("Authorization", bearerToken(author.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    @DisplayName("타인이 모집 상태 변경 → 403")
    void changeStatus_forbidden() throws Exception {
        Map<String, Object> request = Map.of("status", "CLOSED");

        mockMvc.perform(patch("/api/posts/{postId}/status", post.getId())
                        .header("Authorization", bearerToken(other.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}