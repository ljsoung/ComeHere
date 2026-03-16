package com.jiseong.ym.controller;

import com.jiseong.ym.dto.application.ApplicationCreateRequest;
import com.jiseong.ym.dto.application.ApplicationResponse;
import com.jiseong.ym.dto.application.ApplicationStatusUpdateRequest;
import com.jiseong.ym.security.CustomUserDetails;
import com.jiseong.ym.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 지원 API 컨트롤러
 * 모든 엔드포인트는 로그인이 필요하다.
 */
@Tag(name = "지원", description = "지원하기 / 취소 / 지원자 목록 / 승인·거절")
@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * POST /api/posts/{postId}/applications
     * 모집글 지원하기. 로그인한 사용자만 가능.
     */
    @PostMapping("/api/posts/{postId}/applications")
    public ResponseEntity<ApplicationResponse> apply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody ApplicationCreateRequest request) {

        ApplicationResponse response = applicationService.apply(
                userDetails.getUserId(), postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * DELETE /api/applications/{applicationId}
     * 지원 취소. 본인의 지원만 취소 가능.
     */
    @DeleteMapping("/api/applications/{applicationId}")
    public ResponseEntity<Void> cancelApplication(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long applicationId) {

        applicationService.cancelApplication(userDetails.getUserId(), applicationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/posts/{postId}/applications
     * 지원자 목록 조회. 해당 모집글 작성자만 조회 가능.
     */
    @GetMapping("/api/posts/{postId}/applications")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsByPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {

        List<ApplicationResponse> response =
                applicationService.getApplicationsByPost(userDetails.getUserId(), postId);
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/applications/{applicationId}/status
     * 지원 승인 / 거절. 해당 모집글 작성자만 처리 가능.
     */
    @PatchMapping("/api/applications/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {

        ApplicationResponse response = applicationService.updateApplicationStatus(
                userDetails.getUserId(), applicationId, request);
        return ResponseEntity.ok(response);
    }
}