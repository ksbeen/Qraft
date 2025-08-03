package com.example.Qraft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 자격 증명(쿠키 등)을 허용합니다.
        config.setAllowCredentials(true);
        // 프론트엔드 서버 주소(http://localhost:5173)의 요청을 허용합니다.
        config.addAllowedOrigin("http://localhost:5173");
        // 모든 HTTP 헤더를 허용합니다.
        config.addAllowedHeader("*");
        // 모든 HTTP 메소드(GET, POST, PUT, DELETE, OPTIONS 등)를 허용합니다.
        config.addAllowedMethod("*");

        // 모든 URL 경로('/**')에 대해 위 설정을 적용합니다.
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}