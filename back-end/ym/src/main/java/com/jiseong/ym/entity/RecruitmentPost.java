package com.jiseong.ym.entity;

import com.jiseong.ym.entity.common.BaseEntity;
import com.jiseong.ym.entity.enums.MeetingType;
import com.jiseong.ym.entity.enums.PostCategory;
import com.jiseong.ym.entity.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recruitment_posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class RecruitmentPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 포스트 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // 등록자

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 내용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PostCategory category; // 게시글 카테고리

    @Column(nullable = false)
    private int recruitmentCount; // 모집 인원

    @Column(nullable = false, length = 100)
    private String period; // 진행 기간

    @Column(nullable = false)
    private LocalDate deadline; // 모집 마감일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MeetingType meetingType; // 미팅 방식

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private PostStatus status = PostStatus.OPEN; // 상태 (일단 OPEN)

    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0; // 조회수

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostStack> stacks = new ArrayList<>();

    // 게시글 업데이트
    public void update(String title, String content, PostCategory category,
                       int recruitmentCount, String period, LocalDate deadline, MeetingType meetingType) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.recruitmentCount = recruitmentCount;
        this.period = period;
        this.deadline = deadline;
        this.meetingType = meetingType;
    }

    // 상태 변경
    public void changeStatus(PostStatus status) {
        this.status = status;
    }

    // 조회수 증가
    public void increaseViewCount() {
        this.viewCount++;
    }
}