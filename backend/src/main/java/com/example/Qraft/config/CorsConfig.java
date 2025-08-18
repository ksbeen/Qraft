package com.example.Qraft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

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
    /**
     * 정적 리소스(영상 파일 등)에 대한 HTTP 요청을 처리하는 핸들러를 등록합니다.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // '/uploads/**' URL 패턴으로 요청이 오면,
        // 실제 파일 시스템의 'uploads/' 폴더에서 파일을 찾아 제공하라는 설정입니다.
        // "file:./"는 프로젝트 루트 디렉토리(backend 폴더)를 의미합니다.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }
}