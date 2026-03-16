package com.jiseong.ym.dto.application;

import com.jiseong.ym.entity.Application;
import com.jiseong.ym.entity.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 지원 응답 DTO
 * 지원자와 모집글 작성자 모두 사용하는 공통 응답 형식이다.
 * - 지원자: 마이페이지에서 내 지원 현황 확인 시 사용
 * - 작성자: 모집글의 지원자 목록 조회 시 사용
 * applicantUniversity, applicantMajor를 포함하여 작성자가 지원자 정보를 파악할 수 있게 한다.
 */
@Getter
@Builder
public class ApplicationResponse {

    private Long id;
    private Long postId;
    private String postTitle;
    private Long applicantId;
    private String applicantNickname;
    private String applicantUniversity; // 작성자가 지원자 배경 파악용
    private String applicantMajor;      // 작성자가 지원자 전공 파악용
    private String message;             // 지원 메시지
    private ApplicationStatus status;   // PENDING / APPROVED / REJECTED / CANCELED
    private LocalDateTime createdAt;

    /**
     * Application 엔티티 → ApplicationResponse 변환
     */
    public static ApplicationResponse from(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .postId(application.getPost().getId())
                .postTitle(application.getPost().getTitle())
                .applicantId(application.getApplicant().getId())
                .applicantNickname(application.getApplicant().getNickname())
                .applicantUniversity(application.getApplicant().getUniversity())
                .applicantMajor(application.getApplicant().getMajor())
                .message(application.getMessage())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .build();
    }
}