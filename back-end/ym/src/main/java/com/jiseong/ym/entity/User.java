package com.jiseong.ym.entity;

import com.jiseong.ym.entity.common.BaseEntity;
import com.jiseong.ym.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 유저 ID

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private String password; // 패스워드

    @Column(nullable = false, unique = true, length = 100)
    private String nickname; // 닉네임

    @Column(nullable = false, length = 100)
    private String university; // 최종학교

    @Column(nullable = false, length = 100)
    private String major; // 전공

    @Column(columnDefinition = "TEXT")
    private String bio; // 자기소개

    @Column(length = 255)
    private String githubUrl; // 깃허브 링크

    @Column(length = 255)
    private String portfolioUrl; // 포트폴리오 링크

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private UserRole role = UserRole.USER; // 권한 - 사용자

    public void updateProfile(String nickname, String university, String major,
                              String bio, String githubUrl, String portfolioUrl) {
        this.nickname = nickname;
        this.university = university;
        this.major = major;
        this.bio = bio;
        this.githubUrl = githubUrl;
        this.portfolioUrl = portfolioUrl;
    }
}