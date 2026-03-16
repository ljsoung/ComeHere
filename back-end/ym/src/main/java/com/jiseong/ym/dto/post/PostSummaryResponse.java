package com.jiseong.ym.dto.post;

import com.jiseong.ym.entity.PostStack;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.enums.MeetingType;
import com.jiseong.ym.entity.enums.PostCategory;
import com.jiseong.ym.entity.enums.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 모집글 목록 응답 DTO (요약 정보)
 * 목록 페이지에서 카드 형태로 보여줄 핵심 정보만 포함한다.
 * content(본문)처럼 무거운 필드는 제외하여 응답 크기를 줄인다.
 */
@Getter
@Builder
public class PostSummaryResponse {

    private Long id;
    private String title;
    private PostCategory category;
    private int recruitmentCount;
    private LocalDate deadline;
    private MeetingType meetingType;
    private PostStatus status;
    private int viewCount;
    private String authorNickname;
    private List<String> stacks;   // 기술 스택 이름 목록
    private LocalDateTime createdAt;

    /**
     * RecruitmentPost 엔티티 → PostSummaryResponse 변환
     * 이 메서드를 통해 서비스 계층에서 간단하게 변환할 수 있다.
     */
    public static PostSummaryResponse from(RecruitmentPost post) {
        return PostSummaryResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .recruitmentCount(post.getRecruitmentCount())
                .deadline(post.getDeadline())
                .meetingType(post.getMeetingType())
                .status(post.getStatus())
                .viewCount(post.getViewCount())
                .authorNickname(post.getAuthor().getNickname())
                .stacks(post.getStacks().stream()
                        .map(PostStack::getStackName)
                        .toList())
                .createdAt(post.getCreatedAt())
                .build();
    }
}