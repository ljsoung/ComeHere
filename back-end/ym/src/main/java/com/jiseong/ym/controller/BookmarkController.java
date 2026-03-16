package com.jiseong.ym.controller;

import com.jiseong.ym.security.CustomUserDetails;
import com.jiseong.ym.service.BookmarkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 북마크 API 컨트롤러
 * 모든 엔드포인트는 로그인이 필요하다.
 */
@Tag(name = "북마크", description = "북마크 추가 / 해제")
@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * POST /api/posts/{postId}/bookmark
     * 북마크 추가. 이미 북마크한 경우 409 Conflict 반환.
     */
    @PostMapping("/api/posts/{postId}/bookmark")
    public ResponseEntity<Void> addBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {

        bookmarkService.addBookmark(userDetails.getUserId(), postId);
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/posts/{postId}/bookmark
     * 북마크 해제. 북마크가 없으면 404 반환.
     */
    @DeleteMapping("/api/posts/{postId}/bookmark")
    public ResponseEntity<Void> removeBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {

        bookmarkService.removeBookmark(userDetails.getUserId(), postId);
        return ResponseEntity.noContent().build();
    }
}