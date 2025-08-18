package com.example.Qraft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import com.example.Qraft.jwt.JwtAuthenticationFilter; 
import com.example.Qraft.jwt.JwtTokenProvider; 
import lombok.RequiredArgsConstructor;

/**
 * @Configuration: 이 클래스가 Spring의 설정 정보를 담고 있는 클래스임을 나타냅니다.
 * @EnableWebSecurity: Spring Security의 웹 보안 기능을 활성화합니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * PasswordEncoder를 Bean으로 등록합니다.
     * 비밀번호를 암호화하는 데 사용될 구현체로 BCryptPasswordEncoder를 지정합니다.
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security의 핵심적인 보안 설정을 정의하는 SecurityFilterChain을 Bean으로 등록합니다.
     * @param http HttpSecurity 객체. HTTP 관련 보안 설정을 구성하는 데 사용됩니다.
     * @return 구성된 SecurityFilterChain 객체
     * @throws Exception 설정 과정에서 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF(Cross-Site Request Forgery) 공격 방지 기능을 비활성화합니다.
        // JWT 기반의 stateless 인증을 사용하므로, 세션에 의존하는 CSRF 보호는 필요하지 않습니다.
        http.csrf(csrf -> csrf.disable());

        // Spring Security가 세션을 생성하거나 사용하지 않도록 설정합니다. (STATELESS)
        // JWT 인증 방식은 서버가 상태를 저장하지 않는 것을 전제로 하므로 필수적인 설정입니다.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
            // OPTIONS 메소드에 대한 요청은 인증 없이 모두 허용합니다. (Preflight 요청 처리)
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            // '/uploads/' 경로의 모든 GET 요청은 인증 없이 허용합니다.
            .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
            // '/api/users/signup' 과 '/api/users/login' 경로는 인증 없이 접근을 허용합니다.
            .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
            // 그 외의 모든 요청은 반드시 인증(로그인)을 거쳐야 합니다.
            .anyRequest().authenticated()
        );

        // 직접 만든 JwtAuthenticationFilter를 Spring Security의 필터 체인에 추가합니다.
        // UsernamePasswordAuthenticationFilter 이전에 실행되도록 설정합니다.
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // 설정이 완료된 HttpSecurity 객체를 빌드하여 SecurityFilterChain으로 반환합니다.
        return http.build();
    }
}