package com.example.Qraft.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// OncePerRequestFilter: 모든 요청에 대해 한 번만 실행되는 필터를 만듭니다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1. HTTP 요청 헤더에서 JWT를 추출합니다.
        String token = jwtTokenProvider.resolveToken(request);

        // 2. 토큰이 존재하고 유효한지 검증합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰이 유효하면, 토큰에서 인증 정보를 가져옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // 4. 가져온 인증 정보를 SecurityContext에 저장합니다.
            //    이제 Spring Security는 이 사용자가 인증된 사용자임을 알게 됩니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. 다음 필터로 요청을 전달합니다.
        filterChain.doFilter(request, response);
    }
}