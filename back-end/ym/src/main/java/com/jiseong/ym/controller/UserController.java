package com.jiseong.ym.controller;

import com.jiseong.ym.dto.application.ApplicationResponse;
import com.jiseong.ym.dto.bookmark.BookmarkResponse;
import com.jiseong.ym.dto.post.PostSummaryResponse;
import com.jiseong.ym.dto.user.UserResponse;
import com.jiseong.ym.dto.user.UserUpdateRequest;
import com.jiseong.ym.security.CustomUserDetails;
import com.jiseong.ym.service.MyPageService;
import com.jiseong.ym.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 API 컨트롤러
 * - 프로필 조회 / 수정
 * - 마이페이지 (내가 쓴 글 / 지원한 글 / 북마크한 글)
 * 모든 엔드포인트는 로그인이 필요하다.
 */
@Tag(name = "사용자", description = "프로필 조회/수정 / 마이페이지")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MyPageService myPageService;

    /**
     * GET /api/users/me
     * 현재 로그인한 사용자의 프로필 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getMyProfile(userDetails.getUserId()));
    }

    /**
     * PATCH /api/users/me
     * 현재 로그인한 사용자의 프로필 수정
     */
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateMyProfile(userDetails.getUserId(), request));
    }

    /**
     * GET /api/users/me/posts?page=0&size=10
     * 내가 작성한 모집글 목록 (최신순, 페이징)
     */
    @GetMapping("/me/posts")
    public ResponseEntity<Page<PostSummaryResponse>> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(myPageService.getMyPosts(userDetails.getUserId(), page, size));
    }

    /**
     * GET /api/users/me/applications?page=0&size=10
     * 내가 지원한 모집글 목록 (최근 지원순, 페이징)
     */
    @GetMapping("/me/applications")
    public ResponseEntity<Page<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(myPageService.getMyApplications(userDetails.getUserId(), page, size));
    }

    /**
     * GET /api/users/me/bookmarks?page=0&size=10
     * 내가 북마크한 모집글 목록 (최근 북마크순, 페이징)
     */
    @GetMapping("/me/bookmarks")
    public ResponseEntity<Page<BookmarkResponse>> getMyBookmarks(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(myPageService.getMyBookmarks(userDetails.getUserId(), page, size));
    }
}