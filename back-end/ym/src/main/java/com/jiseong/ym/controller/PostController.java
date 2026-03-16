package com.jiseong.ym.controller;

import com.jiseong.ym.dto.post.*;
import com.jiseong.ym.entity.enums.PostCategory;
import com.jiseong.ym.entity.enums.PostStatus;
import com.jiseong.ym.security.CustomUserDetails;
import com.jiseong.ym.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 모집글 API 컨트롤러
 *
 * [비회원 접근 가능] GET /api/posts, GET /api/posts/{postId}
 * [로그인 필요]      POST, PATCH, DELETE
 */
@Tag(name = "모집글", description = "모집글 CRUD / 검색·필터 / 상태 변경")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * POST /api/posts
     * 모집글 작성. 로그인한 사용자만 가능.
     */
    @PostMapping
    public ResponseEntity<PostDetailResponse> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PostCreateRequest request) {
        PostDetailResponse response = postService.createPost(userDetails.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/posts
     * 모집글 목록 조회. 비회원도 접근 가능.
     *
     * @param keyword   제목 키워드 검색
     * @param category  모집 유형 필터 (CONTEST, STUDY 등)
     * @param stackName 기술 스택 필터 (예: "Spring")
     * @param status    모집 상태 필터 (OPEN / CLOSED)
     * @param sort      정렬 기준 ("deadline" → 마감일순, 기본값 → 최신순)
     * @param page      페이지 번호 (0부터 시작, 기본값 0)
     * @param size      페이지 크기 (기본값 10)
     */
    @GetMapping
    public ResponseEntity<Page<PostSummaryResponse>> getPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) PostCategory category,
            @RequestParam(required = false) String stackName,
            @RequestParam(required = false) PostStatus status,
            @RequestParam(required = false, defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostSummaryResponse> response =
                postService.getPosts(keyword, category, stackName, status, sort, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/posts/{postId}
     * 모집글 상세 조회. 비회원도 접근 가능. 조회 시 viewCount 증가.
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    /**
     * PATCH /api/posts/{postId}
     * 모집글 수정. 작성자 본인만 가능.
     */
    @PatchMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> updatePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request) {
        return ResponseEntity.ok(postService.updatePost(userDetails.getUserId(), postId, request));
    }

    /**
     * DELETE /api/posts/{postId}
     * 모집글 삭제. 작성자 본인만 가능.
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {
        postService.deletePost(userDetails.getUserId(), postId);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/posts/{postId}/status
     * 모집 상태 변경 (OPEN ↔ CLOSED). 작성자 본인만 가능.
     */
    @PatchMapping("/{postId}/status")
    public ResponseEntity<PostDetailResponse> changeStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody PostStatusUpdateRequest request) {
        return ResponseEntity.ok(postService.changeStatus(userDetails.getUserId(), postId, request));
    }
}