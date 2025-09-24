package com.example.Qraft.jwt;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.example.Qraft.entity.User;
import com.example.Qraft.service.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;


// @Component: 이 클래스를 Spring 컨테이너에 Bean으로 등록합니다.
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long validityInMilliseconds = 3600000; // 토큰 유효시간: 1시간
    private final CustomUserDetailsService userDetailsService; 


    // 생성자: application.properties에서 jwt.secret 값을 가져와 SecretKey를 생성합니다.
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService userDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    /**
     * 사용자 정보를 바탕으로 JWT를 생성하는 메소드
     * @param user 인증된 사용자 객체
     * @return 생성된 JWT 문자열
     */
    public String createToken(User user) {
        // 토큰에 담을 정보(claims)를 설정합니다.
        // 여기서는 사용자의 이메일을 토큰의 주제(subject)로 설정합니다.
        String subject = user.getEmail();

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .subject(subject) // 토큰의 주제 설정
                .claim("userId", user.getId()) // 사용자 ID 추가
                .claim("nickname", user.getNickname()) // 닉네임 정보 추가
                .issuedAt(now) // 토큰 발행 시간
                .expiration(validity) // 토큰 만료 시간
                .signWith(key) // 비밀 키를 사용하여 서명
                .compact(); // JWT 문자열 생성
    }

    /**
     * HTTP 요청 헤더에서 JWT를 추출하는 메소드
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * JWT가 유효한지 검증하는 메소드
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // 토큰이 유효하지 않을 경우 (만료, 변조 등) false를 반환
            return false;
        }
    }

    /**
     * JWT에서 사용자 정보를 추출하여 Spring Security의 Authentication 객체를 생성하는 메소드
     */
    public Authentication getAuthentication(String token) {
        // 토큰에서 이메일(subject)을 추출합니다.
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        String email = claims.getSubject();

        // userDetailsService를 사용해 DB에서 사용자 정보를 조회합니다.
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Authentication 객체를 생성하여 반환합니다.
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}