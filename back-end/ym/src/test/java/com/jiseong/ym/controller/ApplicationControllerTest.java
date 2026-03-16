package com.jiseong.ym.controller;

import com.jiseong.ym.entity.Application;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.User;
import com.jiseong.ym.entity.enums.ApplicationStatus;
import com.jiseong.ym.entity.enums.PostStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 지원 API 테스트
 * POST   /api/posts/{postId}/applications
 * DELETE /api/applications/{applicationId}
 * GET    /api/posts/{postId}/applications
 * PATCH  /api/applications/{applicationId}/status
 */
class ApplicationControllerTest extends ControllerTestSupport {

    private User author;
    private User applicant;
    private User other;
    private RecruitmentPost post;

    @BeforeEach
    void setUp() {
        author    = createUser("author@test.com", "작성자");
        applicant = createUser("applicant@test.com", "지원자");
        other     = createUser("other@test.com", "타인");
        post      = createPost(author);
    }

    // ─── 지원하기 ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("지원 성공 → 201")
    void apply_success() throws Exception {
        Map<String, Object> request = Map.of("message", "열심히 하겠습니다!");

        mockMvc.perform(post("/api/posts/{postId}/applications", post.getId())
                        .header("Authorization", bearerToken(applicant.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.message").value("열심히 하겠습니다!"));
    }

    @Test
    @DisplayName("마감된 모집글 지원 → 400")
    void apply_closedPost() throws Exception {
        // 모집글 상태를 CLOSED로 변경
        post.changeStatus(PostStatus.CLOSED);

        Map<String, Object> request = Map.of("message", "지원합니다");

        mockMvc.perform(post("/api/posts/{postId}/applications", post.getId())
                        .header("Authorization", bearerToken(applicant.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("마감된 모집글입니다."));
    }

    @Test
    @DisplayName("본인이 작성한 모집글 지원 → 400")
    void apply_ownPost() throws Exception {
        Map<String, Object> request = Map.of("message", "내가 쓴 글");

        mockMvc.perform(post("/api/posts/{postId}/applications", post.getId())
                        .header("Authorization", bearerToken(author.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("본인이 작성한 모집글에는 지원할 수 없습니다."));
    }

    @Test
    @DisplayName("중복 지원 → 409")
    void apply_duplicate() throws Exception {
        // 먼저 지원
        applicationRepository.save(Application.builder()
                .applicant(applicant)
                .post(post)
                .message("첫 번째 지원")
                .build());

        Map<String, Object> request = Map.of("message", "두 번째 지원");

        mockMvc.perform(post("/api/posts/{postId}/applications", post.getId())
                        .header("Authorization", bearerToken(applicant.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 지원한 모집글입니다."));
    }

    // ─── 지원 취소 ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("본인 지원 취소 → 204")
    void cancelApplication_success() throws Exception {
        Application application = applicationRepository.save(Application.builder()
                .applicant(applicant)
                .post(post)
                .build());

        mockMvc.perform(delete("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", bearerToken(applicant.getEmail())))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("타인의 지원 취소 → 403")
    void cancelApplication_forbidden() throws Exception {
        Application application = applicationRepository.save(Application.builder()
                .applicant(applicant)
                .post(post)
                .build());

        mockMvc.perform(delete("/api/applications/{applicationId}", application.getId())
                        .header("Authorization", bearerToken(other.getEmail())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("본인의 지원만 취소할 수 있습니다."));
    }

    // ─── 지원자 목록 조회 ────────────────────────────────────────────────────

    @Test
    @DisplayName("모집글 작성자 - 지원자 목록 조회 → 200")
    void getApplicationsByPost_authorSuccess() throws Exception {
        applicationRepository.save(Application.builder()
                .applicant(applicant)
                .post(post)
                .build());

        mockMvc.perform(get("/api/posts/{postId}/applications", post.getId())
                        .header("Authorization", bearerToken(author.getEmail())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].applicantNickname").value("지원자"));
    }

    @Test
    @DisplayName("비작성자 - 지원자 목록 조회 → 403")
    void getApplicationsByPost_forbidden() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}/applications", post.getId())
                        .header("Authorization", bearerToken(other.getEmail())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("모집글 작성자만 지원을 처리할 수 있습니다."));
    }

    // ─── 지원 승인 / 거절 ────────────────────────────────────────────────────

    @Test
    @DisplayName("지원 승인 → 200, status = APPROVED")
    void updateApplicationStatus_approved() throws Exception {
        Application application = applicationRepository.save(Application.builder()
                .applicant(applicant)
                .post(post)
                .build());

        Map<String, Object> request = Map.of("status", "APPROVED");

        mockMvc.perform(patch("/api/applications/{applicationId}/status", application.getId())
                        .header("Authorization", bearerToken(author.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @DisplayName("지원 거절 → 200, status = REJECTED")
    void updateApplicationStatus_rejected() throws Exception {
        Application application = applicationRepository.save(Application.builder()
                .applicant(applicant)
                .post(post)
                .build());

        Map<String, Object> request = Map.of("status", "REJECTED");

        mockMvc.perform(patch("/api/applications/{applicationId}/status", application.getId())
                        .header("Authorization", bearerToken(author.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    @DisplayName("비작성자가 승인 처리 → 403")
    void updateApplicationStatus_forbidden() throws Exception {
        Application application = applicationRepository.save(Application.builder()
                .applicant(applicant)
                .post(post)
                .build());

        Map<String, Object> request = Map.of("status", "APPROVED");

        mockMvc.perform(patch("/api/applications/{applicationId}/status", application.getId())
                        .header("Authorization", bearerToken(other.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}