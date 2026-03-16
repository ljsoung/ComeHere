package com.jiseong.ym.security;

import com.jiseong.ym.entity.User;
import com.jiseong.ym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security가 인증 시 사용자 정보를 로드하는 서비스
 * JwtAuthenticationFilter에서 토큰의 이메일로 실제 사용자를 DB에서 조회할 때 호출된다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 이메일로 사용자를 조회하여 CustomUserDetails로 감싸 반환
     * username 파라미터는 Spring Security 인터페이스 규약이며, 이 프로젝트에서는 이메일을 의미한다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        return new CustomUserDetails(user);
    }
}