package com.jiseong.ym.controller;

import com.jiseong.ym.entity.Comment;
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
 * 댓글 API 테스트
 * POST   /api/posts/{postId}/comments
 * GET    /api/posts/{postId}/comments
 * PATCH  /api/comments/{commentId}
 * DELETE /api/comments/{commentId}
 */
class CommentControllerTest extends ControllerTestSupport {

    private User commenter;
    private User other;
    private RecruitmentPost post;

    @BeforeEach
    void setUp() {
        commenter = createUser("commenter@test.com", "댓글작성자");
        other     = createUser("other@test.com", "타인");
        post      = createPost(createUser("author@test.com", "작성자"));
    }

    // ─── 댓글 작성 ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("댓글 작성 성공 → 201")
    void createComment_success() throws Exception {
        Map<String, Object> request = Map.of("content", "좋은 글이네요!");

        mockMvc.perform(post("/api/posts/{postId}/comments", post.getId())
                        .header("Authorization", bearerToken(commenter.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("좋은 글이네요!"))
                .andExpect(jsonPath("$.userNickname").value("댓글작성자"));
    }

    @Test
    @DisplayName("내용 없이 댓글 작성 → 400")
    void createComment_emptyContent() throws Exception {
        Map<String, Object> request = Map.of("content", "");

        mockMvc.perform(post("/api/posts/{postId}/comments", post.getId())
                        .header("Authorization", bearerToken(commenter.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 모집글에 댓글 → 404")
    void createComment_postNotFound() throws Exception {
        Map<String, Object> request = Map.of("content", "댓글");

        mockMvc.perform(post("/api/posts/{postId}/comments", 999999L)
                        .header("Authorization", bearerToken(commenter.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ─── 댓글 목록 조회 ──────────────────────────────────────────────────────

    @Test
    @DisplayName("댓글 목록 조회 (비회원 가능) → 200, 오래된순")
    void getComments_success() throws Exception {
        commentRepository.save(Comment.builder()
                .user(commenter).post(post).content("첫 번째 댓글").build());
        commentRepository.save(Comment.builder()
                .user(other).post(post).content("두 번째 댓글").build());

        mockMvc.perform(get("/api/posts/{postId}/comments", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("첫 번째 댓글"));
    }

    // ─── 댓글 수정 ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("본인 댓글 수정 → 200")
    void updateComment_success() throws Exception {
        Comment comment = commentRepository.save(Comment.builder()
                .user(commenter).post(post).content("원래 내용").build());

        Map<String, Object> request = Map.of("content", "수정된 내용");

        mockMvc.perform(patch("/api/comments/{commentId}", comment.getId())
                        .header("Authorization", bearerToken(commenter.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 내용"));
    }

    @Test
    @DisplayName("타인 댓글 수정 → 403")
    void updateComment_forbidden() throws Exception {
        Comment comment = commentRepository.save(Comment.builder()
                .user(commenter).post(post).content("원래 내용").build());

        Map<String, Object> request = Map.of("content", "타인이 수정");

        mockMvc.perform(patch("/api/comments/{commentId}", comment.getId())
                        .header("Authorization", bearerToken(other.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("댓글 작성자만 수행할 수 있습니다."));
    }

    // ─── 댓글 삭제 ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("본인 댓글 삭제 → 204")
    void deleteComment_success() throws Exception {
        Comment comment = commentRepository.save(Comment.builder()
                .user(commenter).post(post).content("삭제할 댓글").build());

        mockMvc.perform(delete("/api/comments/{commentId}", comment.getId())
                        .header("Authorization", bearerToken(commenter.getEmail())))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("타인 댓글 삭제 → 403")
    void deleteComment_forbidden() throws Exception {
        Comment comment = commentRepository.save(Comment.builder()
                .user(commenter).post(post).content("삭제할 댓글").build());

        mockMvc.perform(delete("/api/comments/{commentId}", comment.getId())
                        .header("Authorization", bearerToken(other.getEmail())))
                .andExpect(status().isForbidden());
    }
}