package com.jiseong.ym.dto.user;

import com.jiseong.ym.entity.User;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 정보 응답 DTO
 * User 엔티티를 직접 노출하지 않고, 필요한 필드만 선택해서 반환한다.
 * 비밀번호 등 민감한 정보는 포함하지 않는다.
 */
@Getter
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String nickname;
    private String university;
    private String major;
    private String bio;
    private String githubUrl;
    private String portfolioUrl;

    /**
     * User 엔티티 -> UserResponse 변환
     * 서비스 계층에서 엔티티를 DTO로 변환할 때 사용한다.
     */
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .university(user.getUniversity())
                .major(user.getMajor())
                .bio(user.getBio())
                .githubUrl(user.getGithubUrl())
                .portfolioUrl(user.getPortfolioUrl())
                .build();
    }
}