package com.jiseong.ym.service;

import com.jiseong.ym.dto.application.ApplicationCreateRequest;
import com.jiseong.ym.dto.application.ApplicationResponse;
import com.jiseong.ym.dto.application.ApplicationStatusUpdateRequest;
import com.jiseong.ym.entity.Application;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.User;
import com.jiseong.ym.entity.enums.PostStatus;
import com.jiseong.ym.exception.CustomException;
import com.jiseong.ym.exception.ErrorCode;
import com.jiseong.ym.repository.ApplicationRepository;
import com.jiseong.ym.repository.RecruitmentPostRepository;
import com.jiseong.ym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 지원 기능 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final RecruitmentPostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 모집글 지원하기
     * 비즈니스 규칙 검증 순서:
     *   1. 모집글 존재 여부 확인
     *   2. 마감된 모집글인지 확인 (CLOSED면 지원 불가)
     *   3. 본인이 작성한 모집글인지 확인 (작성자는 지원 불가)
     *   4. 중복 지원 여부 확인
     *   5. 지원 저장
     */
    @Transactional
    public ApplicationResponse apply(Long userId, Long postId, ApplicationCreateRequest request) {
        RecruitmentPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User applicant = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 마감된 모집글 지원 불가
        if (post.getStatus() == PostStatus.CLOSED) {
            throw new CustomException(ErrorCode.POST_CLOSED);
        }

        // 작성자는 본인 모집글에 지원 불가
        if (post.getAuthor().getId().equals(userId)) {
            throw new CustomException(ErrorCode.CANNOT_APPLY_OWN_POST);
        }

        // 동일 모집글 중복 지원 불가
        if (applicationRepository.existsByPostIdAndApplicantId(postId, userId)) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED);
        }

        Application application = Application.builder()
                .applicant(applicant)
                .post(post)
                .message(request.getMessage())
                .build();

        applicationRepository.save(application);

        return ApplicationResponse.from(application);
    }

    /**
     * 지원 취소
     * 본인의 지원만 취소할 수 있다.
     */
    @Transactional
    public void cancelApplication(Long userId, Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));

        // 본인의 지원인지 확인
        if (!application.getApplicant().getId().equals(userId)) {
            throw new CustomException(ErrorCode.APPLICATION_NOT_APPLICANT);
        }

        applicationRepository.delete(application);
    }

    /**
     * 지원자 목록 조회
     * 해당 모집글의 작성자만 조회할 수 있다.
     */
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsByPost(Long userId, Long postId) {
        RecruitmentPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 모집글 작성자만 지원자 목록 조회 가능
        if (!post.getAuthor().getId().equals(userId)) {
            throw new CustomException(ErrorCode.APPLICATION_NOT_AUTHOR);
        }

        return applicationRepository.findByPostId(postId).stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    /**
     * 지원 승인 / 거절
     * 해당 모집글의 작성자만 처리할 수 있다.
     */
    @Transactional
    public ApplicationResponse updateApplicationStatus(
            Long userId, Long applicationId, ApplicationStatusUpdateRequest request) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));

        // 해당 모집글의 작성자인지 확인
        if (!application.getPost().getAuthor().getId().equals(userId)) {
            throw new CustomException(ErrorCode.APPLICATION_NOT_AUTHOR);
        }

        // 엔티티 도메인 메서드로 상태 변경 (더티 체킹으로 자동 반영)
        application.changeStatus(request.getStatus());

        return ApplicationResponse.from(application);
    }
}