package com.example.Qraft.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.Qraft.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 이메일(username)을 기반으로 사용자 정보를 DB에서 찾아 UserDetails 객체로 반환합니다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                // Spring Security의 User 객체로 변환합니다.
                // 첫 번째 인자: username (우리 시스템에서는 이메일)
                // 두 번째 인자: password
                // 세 번째 인자: authorities (권한 목록, 지금은 비워둡니다)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .authorities("USER") // 간단한 권한 부여
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }
}