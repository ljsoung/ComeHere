package com.jiseong.ym.security;

import com.jiseong.ym.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security가 인증에 사용하는 사용자 정보 객체
 * User 엔티티를 감싸서 Security가 요구하는 형태로 제공한다.
 * 컨트롤러에서 @AuthenticationPrincipal CustomUserDetails 로 현재 로그인 사용자를 꺼낼 수 있다.
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * 사용자 권한 목록 반환
     * UserRole(USER, ADMIN)을 "ROLE_USER", "ROLE_ADMIN" 형태로 변환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    // Security가 비밀번호 검증에 사용
    @Override
    public String getPassword() { return user.getPassword(); }

    // Security가 사용자 식별에 사용 (이메일을 username으로 사용)
    @Override
    public String getUsername() { return user.getEmail(); }

    // 서비스 로직에서 현재 사용자 ID가 필요할 때 사용
    public Long getUserId() { return user.getId(); }

    // 서비스 로직에서 User 엔티티가 필요할 때 사용
    public User getUser() { return user; }
}