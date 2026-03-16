package com.jiseong.ym.service;

import com.jiseong.ym.dto.comment.CommentCreateRequest;
import com.jiseong.ym.dto.comment.CommentResponse;
import com.jiseong.ym.dto.comment.CommentUpdateRequest;
import com.jiseong.ym.entity.Comment;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.User;
import com.jiseong.ym.exception.CustomException;
import com.jiseong.ym.exception.ErrorCode;
import com.jiseong.ym.repository.CommentRepository;
import com.jiseong.ym.repository.RecruitmentPostRepository;
import com.jiseong.ym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 댓글 기능 비즈니스 로직
 * MVP에서는 대댓글 없이 일반 댓글만 구현한다.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecruitmentPostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 작성
     * 모집글이 존재하는지 확인 후 댓글을 저장한다.
     */
    @Transactional
    public CommentResponse createComment(Long userId, Long postId, CommentCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        RecruitmentPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();

        commentRepository.save(comment);

        return CommentResponse.from(comment);
    }

    /**
     * 댓글 목록 조회
     * 해당 모집글의 모든 댓글을 오래된 순(작성 시각 오름차순)으로 반환한다.
     * 비회원도 조회 가능하다.
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        // 모집글 존재 여부 확인
        if (!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    /**
     * 댓글 수정
     * 댓글 작성자 본인만 수정할 수 있다.
     */
    @Transactional
    public CommentResponse updateComment(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 댓글 작성자 본인 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_AUTHOR);
        }

        // 엔티티 도메인 메서드로 내용 수정 (더티 체킹으로 자동 반영)
        comment.updateContent(request.getContent());

        return CommentResponse.from(comment);
    }

    /**
     * 댓글 삭제
     * 댓글 작성자 본인만 삭제할 수 있다.
     */
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 댓글 작성자 본인 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_AUTHOR);
        }

        commentRepository.delete(comment);
    }
}