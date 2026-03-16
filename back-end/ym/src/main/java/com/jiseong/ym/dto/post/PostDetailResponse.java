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
 * 모집글 상세 응답 DTO (전체 정보)
 * 상세 페이지에서 필요한 모든 정보를 포함한다.
 * authorId를 포함하여 프론트엔드에서 현재 로그인 사용자와 비교 후 수정/삭제 버튼 노출 여부를 결정한다.
 */
@Getter
@Builder
public class PostDetailResponse {

    private Long id;
    private Long authorId;          // 작성자 ID (프론트에서 권한 버튼 표시 여부 판단용)
    private String authorNickname;
    private String title;
    private String content;         // 본문 전체
    private PostCategory category;
    private int recruitmentCount;
    private String period;          // 활동 기간
    private LocalDate deadline;
    private MeetingType meetingType;
    private PostStatus status;
    private int viewCount;
    private List<String> stacks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * RecruitmentPost 엔티티 → PostDetailResponse 변환
     */
    public static PostDetailResponse from(RecruitmentPost post) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .authorId(post.getAuthor().getId())
                .authorNickname(post.getAuthor().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .recruitmentCount(post.getRecruitmentCount())
                .period(post.getPeriod())
                .deadline(post.getDeadline())
                .meetingType(post.getMeetingType())
                .status(post.getStatus())
                .viewCount(post.getViewCount())
                .stacks(post.getStacks().stream()
                        .map(PostStack::getStackName)
                        .toList())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}