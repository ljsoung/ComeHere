package com.jiseong.ym.dto.comment;

import com.jiseong.ym.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 댓글 응답 DTO
 * userId를 포함하여 프론트엔드에서 현재 로그인 사용자와 비교해 수정/삭제 버튼 표시 여부를 결정한다.
 */
@Getter
@Builder
public class CommentResponse {

    private Long id;
    private Long postId;
    private Long userId;            // 댓글 작성자 ID (프론트에서 본인 댓글 여부 판단용)
    private String userNickname;    // 댓글 작성자 닉네임
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Comment 엔티티 → CommentResponse 변환
     */
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}