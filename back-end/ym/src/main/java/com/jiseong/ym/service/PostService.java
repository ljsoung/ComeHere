package com.jiseong.ym.service;

import com.jiseong.ym.dto.post.*;
import com.jiseong.ym.entity.PostStack;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.User;
import com.jiseong.ym.entity.enums.PostCategory;
import com.jiseong.ym.entity.enums.PostStatus;
import com.jiseong.ym.exception.CustomException;
import com.jiseong.ym.exception.ErrorCode;
import com.jiseong.ym.repository.PostStackRepository;
import com.jiseong.ym.repository.RecruitmentPostRepository;
import com.jiseong.ym.repository.UserRepository;
import com.jiseong.ym.repository.spec.RecruitmentPostSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 모집글 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final RecruitmentPostRepository postRepository;
    private final PostStackRepository postStackRepository;
    private final UserRepository userRepository;

    /**
     * 모집글 작성
     * 1. 게시글 저장
     * 2. 기술 스택을 별도 테이블(post_stacks)에 저장
     */
    @Transactional
    public PostDetailResponse createPost(Long userId, PostCreateRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 모집글 엔티티 생성 및 저장
        RecruitmentPost post = RecruitmentPost.builder()
                .author(author)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .recruitmentCount(request.getRecruitmentCount())
                .period(request.getPeriod())
                .deadline(request.getDeadline())
                .meetingType(request.getMeetingType())
                .build();

        postRepository.save(post);

        // 기술 스택 저장 (요청에 스택이 있는 경우에만)
        saveStacks(post, request.getStacks());

        return PostDetailResponse.from(post);
    }

    /**
     * 모집글 목록 조회 (검색 + 필터 + 정렬 + 페이징)
     * sort 파라미터: "deadline" → 마감일순, 그 외 → 최신순(기본값)
     */
    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getPosts(
            String keyword, PostCategory category, String stackName,
            PostStatus status, String sort, int page, int size) {

        // 정렬 기준 결정
        Sort sortOrder = "deadline".equals(sort)
                ? Sort.by("deadline").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        // 동적 검색 조건 생성
        Specification<RecruitmentPost> spec =
                RecruitmentPostSpec.withCondition(keyword, category, stackName, status);

        return postRepository.findAll(spec, pageable)
                .map(PostSummaryResponse::from);
    }

    /**
     * 모집글 상세 조회
     * 조회할 때마다 조회수를 1 증가시킨다.
     */
    @Transactional
    public PostDetailResponse getPost(Long postId) {
        RecruitmentPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 조회수 증가 (엔티티 도메인 메서드 활용)
        post.increaseViewCount();

        return PostDetailResponse.from(post);
    }

    /**
     * 모집글 수정
     * 1. 작성자 본인인지 확인
     * 2. 기존 기술 스택 전체 삭제 후 새로 등록 (교체 방식)
     * 3. 게시글 내용 수정
     */
    @Transactional
    public PostDetailResponse updatePost(Long userId, Long postId, PostUpdateRequest request) {
        RecruitmentPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 작성자 본인 확인
        validateAuthor(post, userId);

        // 기존 스택 삭제 후 새 스택 등록
        postStackRepository.deleteByPost(post);
        saveStacks(post, request.getStacks());

        // 모집글 내용 수정 (더티 체킹으로 자동 반영)
        post.update(
                request.getTitle(),
                request.getContent(),
                request.getCategory(),
                request.getRecruitmentCount(),
                request.getPeriod(),
                request.getDeadline(),
                request.getMeetingType()
        );

        return PostDetailResponse.from(post);
    }

    /**
     * 모집글 삭제
     * 작성자 본인만 삭제 가능하다.
     * post_stacks, applications, bookmarks, comments는 CASCADE 또는 FK 제약으로 함께 삭제된다.
     */
    @Transactional
    public void deletePost(Long userId, Long postId) {
        RecruitmentPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        validateAuthor(post, userId);

        postRepository.delete(post);
    }

    /**
     * 모집 상태 변경 (OPEN ↔ CLOSED)
     * 작성자 본인만 변경 가능하다.
     */
    @Transactional
    public PostDetailResponse changeStatus(Long userId, Long postId, PostStatusUpdateRequest request) {
        RecruitmentPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        validateAuthor(post, userId);

        // 엔티티 도메인 메서드로 상태 변경
        post.changeStatus(request.getStatus());

        return PostDetailResponse.from(post);
    }

    // ─── private 헬퍼 메서드 ──────────────────────────────────────────────────

    /**
     * 현재 사용자가 게시글 작성자인지 확인
     * 아닌 경우 403 Forbidden 예외 발생
     */
    private void validateAuthor(RecruitmentPost post, Long userId) {
        if (!post.getAuthor().getId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_NOT_AUTHOR);
        }
    }

    /**
     * 기술 스택 리스트를 PostStack 엔티티로 변환하여 저장
     */
    private void saveStacks(RecruitmentPost post, List<String> stackNames) {
        if (stackNames == null || stackNames.isEmpty()) return;

        List<PostStack> stacks = stackNames.stream()
                .map(name -> PostStack.builder()
                        .post(post)
                        .stackName(name)
                        .build())
                .toList();

        postStackRepository.saveAll(stacks);
    }
}