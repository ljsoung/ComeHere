package com.jiseong.ym.controller;

import com.jiseong.ym.entity.Bookmark;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 북마크 API 테스트
 * POST   /api/posts/{postId}/bookmark
 * DELETE /api/posts/{postId}/bookmark
 */
class BookmarkControllerTest extends ControllerTestSupport {

    private User user;
    private RecruitmentPost post;

    @BeforeEach
    void setUp() {
        user = createUser("user@test.com", "유저");
        post = createPost(createUser("author@test.com", "작성자"));
    }

    @Test
    @DisplayName("북마크 추가 → 200")
    void addBookmark_success() throws Exception {
        mockMvc.perform(post("/api/posts/{postId}/bookmark", post.getId())
                        .header("Authorization", bearerToken(user.getEmail())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이미 북마크한 글 재북마크 → 409")
    void addBookmark_duplicate() throws Exception {
        // 이미 북마크 추가
        bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .post(post)
                .build());

        mockMvc.perform(post("/api/posts/{postId}/bookmark", post.getId())
                        .header("Authorization", bearerToken(user.getEmail())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 북마크한 모집글입니다."));
    }

    @Test
    @DisplayName("북마크 해제 → 204")
    void removeBookmark_success() throws Exception {
        bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .post(post)
                .build());

        mockMvc.perform(delete("/api/posts/{postId}/bookmark", post.getId())
                        .header("Authorization", bearerToken(user.getEmail())))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("북마크하지 않은 글 해제 → 404")
    void removeBookmark_notFound() throws Exception {
        mockMvc.perform(delete("/api/posts/{postId}/bookmark", post.getId())
                        .header("Authorization", bearerToken(user.getEmail())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("북마크를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("비인증 북마크 추가 → 4xx")
    void addBookmark_unauthorized() throws Exception {
        mockMvc.perform(post("/api/posts/{postId}/bookmark", post.getId()))
                .andExpect(status().is4xxClientError());
    }
}