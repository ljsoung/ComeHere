package com.jiseong.ym.dto.bookmark;

import com.jiseong.ym.entity.Bookmark;
import com.jiseong.ym.entity.enums.PostCategory;
import com.jiseong.ym.entity.enums.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 북마크 응답 DTO
 * 마이페이지 북마크 목록에서 어떤 글을 북마크했는지 보여줄 때 사용한다.
 * 북마크 자체 ID와 함께 모집글의 핵심 정보를 포함한다.
 */
@Getter
@Builder
public class BookmarkResponse {

    private Long bookmarkId;        // 북마크 ID (북마크 해제 시 사용)
    private Long postId;
    private String postTitle;
    private PostCategory postCategory;
    private PostStatus postStatus;
    private LocalDate postDeadline;
    private String authorNickname;
    private LocalDateTime bookmarkedAt; // 북마크한 시각

    /**
     * Bookmark 엔티티 → BookmarkResponse 변환
     */
    public static BookmarkResponse from(Bookmark bookmark) {
        return BookmarkResponse.builder()
                .bookmarkId(bookmark.getId())
                .postId(bookmark.getPost().getId())
                .postTitle(bookmark.getPost().getTitle())
                .postCategory(bookmark.getPost().getCategory())
                .postStatus(bookmark.getPost().getStatus())
                .postDeadline(bookmark.getPost().getDeadline())
                .authorNickname(bookmark.getPost().getAuthor().getNickname())
                .bookmarkedAt(bookmark.getCreatedAt())
                .build();
    }
}