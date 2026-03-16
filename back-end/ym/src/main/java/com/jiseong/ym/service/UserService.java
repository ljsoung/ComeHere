package com.jiseong.ym.service;

import com.jiseong.ym.dto.user.UserResponse;
import com.jiseong.ym.dto.user.UserUpdateRequest;
import com.jiseong.ym.entity.User;
import com.jiseong.ym.exception.CustomException;
import com.jiseong.ym.exception.ErrorCode;
import com.jiseong.ym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 내 프로필 조회 / 수정 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 내 프로필 조회
     * JWT에서 추출한 userId로 사용자를 조회하여 반환한다.
     */
    @Transactional(readOnly = true)
    public UserResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    /**
     * 내 프로필 수정
     * 1. 닉네임을 변경하는 경우, 다른 사용자와 중복 여부 확인
     * 2. User 엔티티의 updateProfile() 메서드로 변경 (더티 체킹으로 자동 저장)
     */
    @Transactional
    public UserResponse updateMyProfile(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 닉네임이 바뀐 경우에만 중복 체크 (현재 닉네임과 같으면 검사 불필요)
        if (!user.getNickname().equals(request.getNickname()) &&
                userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        }

        // 엔티티의 도메인 메서드 호출 — @Transactional 이므로 save() 없이 자동 반영
        user.updateProfile(
                request.getNickname(),
                request.getUniversity(),
                request.getMajor(),
                request.getBio(),
                request.getGithubUrl(),
                request.getPortfolioUrl()
        );

        return UserResponse.from(user);
    }
}