package com.jiseong.ym.service;

import com.jiseong.ym.dto.auth.LoginRequest;
import com.jiseong.ym.dto.auth.LoginResponse;
import com.jiseong.ym.dto.auth.SignUpRequest;
import com.jiseong.ym.entity.User;
import com.jiseong.ym.exception.CustomException;
import com.jiseong.ym.exception.ErrorCode;
import com.jiseong.ym.repository.UserRepository;
import com.jiseong.ym.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원가입 / 로그인 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     * 1. 이메일 중복 체크
     * 2. 닉네임 중복 체크
     * 3. 비밀번호 암호화 후 저장
     */
    @Transactional
    public void signUp(SignUpRequest request) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        // 닉네임 중복 검사
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        }

        // 비밀번호를 BCrypt로 암호화하여 저장
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .university(request.getUniversity())
                .major(request.getMajor())
                .build();

        userRepository.save(user);
    }

    /**
     * 로그인
     * 1. 이메일로 사용자 조회
     * 2. 비밀번호 일치 여부 확인
     * 3. JWT 토큰 발급 후 반환
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 입력한 비밀번호와 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // JWT 토큰 생성 후 반환
        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new LoginResponse(token);
    }
}