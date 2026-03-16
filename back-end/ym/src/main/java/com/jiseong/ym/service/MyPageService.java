package com.jiseong.ym.service;

import com.jiseong.ym.dto.application.ApplicationResponse;
import com.jiseong.ym.dto.bookmark.BookmarkResponse;
import com.jiseong.ym.dto.post.PostSummaryResponse;
import com.jiseong.ym.repository.ApplicationRepository;
import com.jiseong.ym.repository.BookmarkRepository;
import com.jiseong.ym.repository.RecruitmentPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 마이페이지 비즈니스 로직
 * 내가 작성한 글 / 지원한 글 / 북마크한 글을 페이징하여 조회한다.
 * 모두 readOnly 트랜잭션으로 처리한다.
 */
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final RecruitmentPostRepository postRepository;
    private final ApplicationRepository applicationRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     * 내가 작성한 모집글 목록
     * 최신순으로 페이징하여 반환한다.
     */
    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getMyPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByAuthorId(userId, pageable)
                .map(PostSummaryResponse::from);
    }

    /**
     * 내가 지원한 모집글 목록
     * 최근 지원 순으로 페이징하여 반환한다.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getMyApplications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return applicationRepository.findByApplicantId(userId, pageable)
                .map(ApplicationResponse::from);
    }

    /**
     * 내가 북마크한 모집글 목록
     * 최근 북마크 순으로 페이징하여 반환한다.
     */
    @Transactional(readOnly = true)
    public Page<BookmarkResponse> getMyBookmarks(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return bookmarkRepository.findByUserId(userId, pageable)
                .map(BookmarkResponse::from);
    }
}