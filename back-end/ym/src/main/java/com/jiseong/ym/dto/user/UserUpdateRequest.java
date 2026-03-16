package com.jiseong.ym.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 내 프로필 수정 요청 DTO
 * 필수 항목(닉네임, 학교, 전공)은 @NotBlank로 검증하고,
 * 선택 항목(자기소개, GitHub, 포트폴리오)은 null 허용한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "학교명을 입력해주세요.")
    private String university;

    @NotBlank(message = "전공을 입력해주세요.")
    private String major;

    private String bio;             // 자기소개 (선택)
    private String githubUrl;       // GitHub 링크 (선택)
    private String portfolioUrl;    // 포트폴리오 링크 (선택)
}