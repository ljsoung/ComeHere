package com.jiseong.ym.service;

import com.jiseong.ym.entity.Bookmark;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.User;
import com.jiseong.ym.exception.CustomException;
import com.jiseong.ym.exception.ErrorCode;
import com.jiseong.ym.repository.BookmarkRepository;
import com.jiseong.ym.repository.RecruitmentPostRepository;
import com.jiseong.ym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 북마크 기능 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final RecruitmentPostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 북마크 추가
     * 이미 북마크한 게시글에 다시 북마크하면 예외를 발생시킨다.
     */
    @Transactional
    public void addBookmark(Long userId, Long postId) {
        // 중복 북마크 체크
        if (bookmarkRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new CustomException(ErrorCode.ALREADY_BOOKMARKED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        RecruitmentPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .post(post)
                .build();

        bookmarkRepository.save(bookmark);
    }

    /**
     * 북마크 해제
     * 북마크가 존재하지 않으면 404 예외를 발생시킨다.
     */
    @Transactional
    public void removeBookmark(Long userId, Long postId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
    }
}