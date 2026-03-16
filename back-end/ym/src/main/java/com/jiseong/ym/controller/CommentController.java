package com.jiseong.ym.controller;

import com.jiseong.ym.dto.comment.CommentCreateRequest;
import com.jiseong.ym.dto.comment.CommentResponse;
import com.jiseong.ym.dto.comment.CommentUpdateRequest;
import com.jiseong.ym.security.CustomUserDetails;
import com.jiseong.ym.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글 API 컨트롤러
 * 댓글 목록 조회는 비회원도 가능하고, 나머지는 로그인이 필요하다.
 */
@Tag(name = "댓글", description = "댓글 작성 / 목록 조회 / 수정 / 삭제")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * POST /api/posts/{postId}/comments
     * 댓글 작성. 로그인한 사용자만 가능.
     */
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request) {

        CommentResponse response = commentService.createComment(
                userDetails.getUserId(), postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/posts/{postId}/comments
     * 댓글 목록 조회. 비회원도 접근 가능. 오래된 순으로 반환.
     */
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

    /**
     * PATCH /api/comments/{commentId}
     * 댓글 수정. 댓글 작성자 본인만 가능.
     */
    @PatchMapping("/api/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request) {

        return ResponseEntity.ok(
                commentService.updateComment(userDetails.getUserId(), commentId, request));
    }

    /**
     * DELETE /api/comments/{commentId}
     * 댓글 삭제. 댓글 작성자 본인만 가능.
     */
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId) {

        commentService.deleteComment(userDetails.getUserId(), commentId);
        return ResponseEntity.noContent().build();
    }
}