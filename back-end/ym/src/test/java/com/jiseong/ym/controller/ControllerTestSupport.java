package com.jiseong.ym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.User;
import com.jiseong.ym.entity.enums.MeetingType;
import com.jiseong.ym.entity.enums.PostCategory;
import com.jiseong.ym.repository.*;
import com.jiseong.ym.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * 컨트롤러 통합 테스트 공통 베이스 클래스
 * - H2 인메모리 DB 사용 (application-test.properties)
 * - @Transactional: 각 테스트 후 DB 롤백 → 테스트 간 데이터 격리
 * - MockMvc: 실제 서버 없이 HTTP 요청/응답 시뮬레이션
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class ControllerTestSupport {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;

    @Autowired protected UserRepository userRepository;
    @Autowired protected RecruitmentPostRepository postRepository;
    @Autowired protected ApplicationRepository applicationRepository;
    @Autowired protected BookmarkRepository bookmarkRepository;
    @Autowired protected CommentRepository commentRepository;

    @Autowired protected PasswordEncoder passwordEncoder;
    @Autowired protected JwtTokenProvider jwtTokenProvider;

    // ─── 공통 헬퍼 메서드 ───────────────────────────────────────────────────

    /** JWT 토큰 생성 (Authorization 헤더에 사용) */
    protected String bearerToken(String email) {
        return "Bearer " + jwtTokenProvider.generateToken(email);
    }

    /** 테스트용 사용자 생성 */
    protected User createUser(String email, String nickname) {
        return userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode("password123"))
                .nickname(nickname)
                .university("테스트대학교")
                .major("컴퓨터공학과")
                .build());
    }

    /** 테스트용 모집글 생성 */
    protected RecruitmentPost createPost(User author) {
        return postRepository.save(RecruitmentPost.builder()
                .author(author)
                .title("백엔드 팀원 모집")
                .content("Spring Boot 프로젝트 같이 해요")
                .category(PostCategory.STUDY)
                .recruitmentCount(3)
                .period("3개월")
                .deadline(LocalDate.now().plusDays(7))
                .meetingType(MeetingType.ONLINE)
                .build());
    }
}